package DiogoRangel.Bar.service;

import DiogoRangel.Bar.classes.*;
import DiogoRangel.Bar.dto.*;
import DiogoRangel.Bar.model.*;
import DiogoRangel.Bar.exception.*;
import DiogoRangel.Bar.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final MesaRepository mesaRepository;
        private final ContaService contaService;

    // Injeção de dependências no construtor
    public ClienteService(ClienteRepository clienteRepository, ConfiguracaoRepository configuracaoRepository, MesaRepository mesaRepository, ContaService contaService) {
        this.clienteRepository = clienteRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.mesaRepository = mesaRepository;
        this.contaService = contaService;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ItemInexistente("Cliente não encontrado com id: " + id));
    }

    public Cliente buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadosInvalidos("CPF não pode ser vazio");
        }

        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ItemInexistente("Cliente não encontrado com CPF: " + cpf));
    }

    public Cliente cadastrar(String nome, String cpf) {
        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidos("Nome do cliente é obrigatório");
        }

        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadosInvalidos("CPF do cliente é obrigatório");
        }

        // Verificar se CPF já existe
        if (clienteRepository.existsByCpf(cpf)) {
            throw new ItemJaCadastrado("CPF já cadastrado: " + cpf);
        }

        Cliente cliente = new Cliente(nome, cpf);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, String nome, String cpf) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ItemInexistente("Cliente não encontrado com id: " + id));

        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadosInvalidos("Nome do cliente é obrigatório");
        }

        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadosInvalidos("CPF do cliente é obrigatório");
        }

        // Verificar se o novo CPF já existe em outro cliente
        if (!cliente.getCpf().equals(cpf) && clienteRepository.existsByCpf(cpf)) {
            throw new ItemJaCadastrado("CPF já cadastrado: " + cpf);
        }

        cliente.setNome(nome);
        cliente.setCpf(cpf);

        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ItemInexistente("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }
    public ContaDetalhesDTO consultarContaPorToken(String tokenMesa) {
        Mesa mesa = mesaRepository.findByTokenAndContaAtivaIsTrue(tokenMesa)
                .orElseThrow(() -> new ItemInexistente("Mesa não encontrada ou conta inativa para o token fornecido."));

        Conta contaAtiva = mesa.getContas().stream()
                .filter(Conta::isEstaAberta)
                .findFirst()
                .orElseThrow(() -> new ItemInexistente("Conta não encontrada ou fechada para esta mesa."));

        int numPessoas = mesa.getNumPessoas();
        Long contaId = contaAtiva.getId();

        // --- Cálculo dos detalhes para o DTO ---
        Configuracao config = configuracaoRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada."));

        double valorGorjeta = contaAtiva.calcularGorjeta(config.getPercentualGorjetaBebida(), config.getPercentualGorjetaComida());
        double valorCouvert = config.getPrecoCouvertPorPessoa() * numPessoas;

        // Reutiliza a lógica de cálculo total e pendente do ContaService
        double valorTotal = contaService.calcularTotalDaConta(contaId, numPessoas);
        double valorPendente = contaService.calcularValorPendente(contaId, numPessoas);

        return new ContaDetalhesDTO(contaAtiva, valorTotal, valorPendente, valorGorjeta, valorCouvert);
    }
}
