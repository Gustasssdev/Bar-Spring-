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
@Transactional
public class GarcomService {

    private final MesaRepository mesaRepository;
    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final ConsumoRepository consumoRepository;

    // 1. ADICIONADO: Injeção do ContaService
    private final ContaService contaService;

    // Injeção de dependências no construtor (Atualizado)
    public GarcomService(MesaRepository mesaRepository, ContaRepository contaRepository, ClienteRepository clienteRepository, ItemCardapioRepository itemCardapioRepository, ConsumoRepository consumoRepository, ContaService contaService)
    {
        this.mesaRepository = mesaRepository;
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
        this.itemCardapioRepository = itemCardapioRepository;
        this.consumoRepository = consumoRepository;
        this.contaService = contaService; // Injeção
    }

    public Mesa abrirMesa(int numero, int numeroPessoas, Long garcomId, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new DadosInvalidos("Cliente não encontrado."));

        Mesa mesa = new Mesa(numero, UUID.randomUUID().toString(), numeroPessoas);
        mesa = mesaRepository.save(mesa);

        Conta conta = new Conta(1, cliente, mesa);
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
        conta.getConsumos().add(consumo);
        consumoRepository.save(consumo);

        return consumo;
    }

    public void cancelarConsumo(Long consumoId, String motivo, Long garcomId) {
        Consumo consumo = consumoRepository.findById(consumoId)
                .orElseThrow(() -> new ItemInexistente("Consumo não encontrado."));

        if (!consumo.getConta().isEstaAberta()) {
            throw new ContaFechada("Não é possível cancelar itens em uma conta fechada.");
        }
        consumo.cancelar(motivo);
        consumoRepository.save(consumo);
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

        conta.getPagamentos().add(valorPago);

        return contaRepository.save(conta);
    }

    public Conta fecharConta(Long contaId, Long garcomId) {
        //Busca a conta para obter o número de pessoas
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        return contaService.fecharConta(contaId, conta.getMesa().getNumPessoas(), garcomId);
    }
}