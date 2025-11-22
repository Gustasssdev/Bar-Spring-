package DiogoRangel.Bar.service;

import DiogoRangel.Bar.model.*;
import DiogoRangel.Bar.classes.*;
import DiogoRangel.Bar.exception.*;
import DiogoRangel.Bar.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID; // Geração de tokens

@Service
@Transactional // Garante que as operações são atômicas
public class GarcomService {

    private final MesaRepository mesaRepository;
    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final ConsumoRepository consumoRepository;

    // Injeção de dependências no construtor
    public GarcomService(MesaRepository mesaRepository, ContaRepository contaRepository, ClienteRepository clienteRepository, ItemCardapioRepository itemCardapioRepository, ConsumoRepository consumoRepository)
    {
        this.mesaRepository = mesaRepository;
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.consumoRepository = consumoRepository;
    }

    public Mesa abrirMesa(int numero, int numeroPessoas, Long garcomId, Long clienteId) {
        // 1. Busca Cliente e Garçom (Usuário)
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new DadosInvalidos("Cliente não encontrado."));

        // 2. Cria a Mesa com o token e número de pessoas
        Mesa mesa = new Mesa(numero, UUID.randomUUID().toString(), numeroPessoas);
        mesa = mesaRepository.save(mesa);

        // 3. Abre a primeira Conta e associa à Mesa
        Conta conta = new Conta(1, cliente, mesa); // numConta inicial como 1
        contaRepository.save(conta);

        mesa.getContas().add(conta);
        return mesaRepository.save(mesa);
    }
    public Consumo adicionarItem(Long contaId, Long itemId, int quantidade, Long garcomId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        if (!conta.isEstaAberta()) {
            throw new ContaFechada("Conta já está fechada. Não é possível adicionar itens.");
        }

        ItemCardapio item = itemCardapioRepository.findById(itemId)
                .orElseThrow(() -> new ItemInexistente("Item do cardápio não encontrado."));

        Consumo consumo = new Consumo(item, quantidade, conta);
        // consumo.setGarcomLancamentoId(garcomId); // Se estiver rastreando o Garçom

        // Adiciona à lista da conta e salva
        conta.getConsumos().add(consumo);
        consumoRepository.save(consumo);
        contaRepository.save(conta);

        return consumo;
    }
    public void cancelarConsumo(Long consumoId, String motivo, Long garcomId) {
        Consumo consumo = consumoRepository.findById(consumoId)
                .orElseThrow(() -> new ItemInexistente("Consumo não encontrado."));

        // Valida se a conta está aberta antes de permitir o cancelamento
        if (!consumo.getConta().isEstaAberta()) {
            throw new ContaFechada("Não é possível cancelar itens em uma conta fechada.");
        }

        // A lógica de negócio reside no Model Consumo
        // consumo.setGarcomCancelamentoId(garcomId); // Rastreamento
        consumo.cancelar(motivo);

        consumoRepository.save(consumo);
        // Não é necessário salvar a conta, pois o Consumo::getValorTotal() já retorna 0.0 após o cancelamento.
    }
    public Conta registrarPagamento(Long contaId, double valorPago, Long garcomId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        if (!conta.isEstaAberta()) {
            throw new ContaFechada("Conta já está fechada. Pagamento não pode ser registrado.");
        }

        if (valorPago <= 0) {
            throw new DadosInvalidos("O valor do pagamento deve ser positivo.");
        }

        // Adiciona o valor à lista de pagamentos da entidade Conta
        conta.getPagamentos().add(valorPago);
        // conta.getPagamentosGarcomId().add(garcomId); // Rastreamento opcional

        return contaRepository.save(conta);
    }
    public Conta fecharConta(Long contaId, Long garcomId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        if (!conta.isEstaAberta()) {
            return conta; // Já está fechada
        }

        try {
            double valorPendente = conta.getValorPendente();

            if (valorPendente > 0) {
                // Lança uma exceção se a regra de negócio for violada
                throw new IllegalStateException("A conta não pode ser fechada. Valor pendente: R$" + valorPendente);
            }

            // A lógica de fechar (estaAberta = false) reside no Model Conta
            conta.fecharConta();
            // conta.setGarcomFechamentoId(garcomId); // Rastreamento

        } catch (PagamentoMaior e) {
            // Trata o caso de PagamentoMaior (se o valor total pago exceder o total da conta)
            // Isso normalmente acontece se for pago em excesso sem fechar a conta
            throw new IllegalStateException("A conta não pode ser fechada devido a um erro: " + e.getMessage());
        }

        return contaRepository.save(conta);
    }
}