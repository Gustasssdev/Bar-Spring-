package DiogoRangel.Bar.service;

import DiogoRangel.Bar.dto.ItemFaturamentoDTO;
import DiogoRangel.Bar.dto.ItemVendaDTO;
import DiogoRangel.Bar.exception.DadosInvalidos;
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


}