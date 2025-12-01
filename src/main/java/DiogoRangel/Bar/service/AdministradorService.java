package DiogoRangel.Bar.service;

import DiogoRangel.Bar.classes.Conta;
import DiogoRangel.Bar.dto.CancelarGorjetaResponseDTO;
import DiogoRangel.Bar.dto.ItemFaturamentoDTO;
import DiogoRangel.Bar.dto.ItemVendaDTO;
import DiogoRangel.Bar.exception.ContaFechada;
import DiogoRangel.Bar.exception.ContaInexistente;
import DiogoRangel.Bar.exception.DadosInvalidos;
import DiogoRangel.Bar.exception.OperacaoNaoPermitida;
import DiogoRangel.Bar.model.Configuracao;
import DiogoRangel.Bar.model.Mesa;
import DiogoRangel.Bar.classes.ItemCardapio;
import DiogoRangel.Bar.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdministradorService {

    private final ConfiguracaoRepository configRepository;
    private final MesaRepository mesaRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final ContaRepository contaRepository;
    private final ConsumoRepository consumoRepository;

    public AdministradorService(ConfiguracaoRepository configRepository, MesaRepository mesaRepository,
                                ItemCardapioRepository itemCardapioRepository, ContaRepository contaRepository, ConsumoRepository consumoRepository) {
        this.configRepository = configRepository;
        this.mesaRepository = mesaRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.contaRepository = contaRepository;
        this.consumoRepository = consumoRepository;
    }

    public Configuracao atualizarRegras(double couvert, double percBebida, double percComida) throws DadosInvalidos {
        // Usa o ID fixo 1L, buscando a configuração única
        Configuracao config = configRepository.findById(1L).orElse(new Configuracao());

        if(couvert < 0 || percBebida < 0 || percComida < 0)
            throw new DadosInvalidos("Não é permitido valores negativos!");
        config.setPrecoCouvertPorPessoa(couvert);
        config.setPercentualGorjetaBebida(percBebida);
        config.setPercentualGorjetaComida(percComida);

        return configRepository.save(config);
    }

    public Mesa cadastrarMesa(Mesa novaMesa) {
        return mesaRepository.save(novaMesa);
    }
    public ItemCardapio cadastrarItem(ItemCardapio novoItem) throws DadosInvalidos{
        if(novoItem.getValor() <= 0)
            throw new DadosInvalidos("Não é permitido valor negativo ou zerado!");
        return itemCardapioRepository.save(novoItem);
    }

    public double gerarRelatorioFaturamento(LocalDateTime inicio, LocalDateTime fim) {
        //Soma o 'totalPago' em todas as contas fechadas no período
        Double total = contaRepository.sumTotalPagoByDataFechamentoBetween(inicio, fim)
                .orElse(0.0);
        return total;
    }

    // 2. Itens Mais Vendidos (pela quantidade)
    public List<ItemVendaDTO> gerarRelatorioItensMaisVendidos() {
        return consumoRepository.findItensMaisVendidos();
    }

    public List<ItemFaturamentoDTO> gerarRelatorioItensMaiorFaturamento() {
        return consumoRepository.findItensComMaiorFaturamento();
    }

    /**
     * Cancela a gorjeta de uma conta específica.
     * Apenas contas com ticket = 1 (entrada paga) podem ter a gorjeta cancelada.
     * Apenas administradores podem executar esta ação.
     * 
     * @param contaId ID da conta
     * @return DTO com informações sobre o cancelamento
     */
    public CancelarGorjetaResponseDTO cancelarGorjeta(Long contaId) {
        // 1. Buscar a conta
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        // 2. Verificar se a conta está aberta (não permitir cancelamento em conta fechada)
        if (!conta.isEstaAberta()) {
            throw new ContaFechada("Não é possível cancelar a gorjeta de uma conta já fechada.");
        }

        // 3. Verificar se a conta possui ticket = 1 (entrada paga)
        if (conta.getTicket() != 1) {
            throw new OperacaoNaoPermitida("Apenas contas com entrada paga (ticket = 1) podem ter a gorjeta cancelada.");
        }

        // 4. Verificar se a gorjeta já foi cancelada
        if (conta.isGorjetaCancelada()) {
            throw new OperacaoNaoPermitida("A gorjeta desta conta já foi cancelada anteriormente.");
        }

        // 5. Buscar configuração para calcular a gorjeta antes do cancelamento
        Configuracao config = configRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada."));

        // 6. Calcular o valor da gorjeta que será removida (antes de cancelar)
        double gorjetaRemovida = conta.calcularGorjeta(
                config.getPercentualGorjetaBebida(),
                config.getPercentualGorjetaComida()
        );

        // 7. Cancelar a gorjeta
        conta.setGorjetaCancelada(true);
        contaRepository.save(conta);

        // 8. Calcular o novo total da conta (sem gorjeta)
        int numPessoas = conta.getMesa().getNumPessoas();
        double totalItens = conta.calcularTotalConsumido();
        double couvert = config.getPrecoCouvertPorPessoa() * numPessoas;
        double novoTotal = totalItens + couvert; // Gorjeta agora é 0

        // 9. Retornar resposta
        return new CancelarGorjetaResponseDTO(
                contaId,
                "Gorjeta cancelada com sucesso.",
                gorjetaRemovida,
                novoTotal,
                true
        );
    }

}