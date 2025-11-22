package DiogoRangel.Bar.service;

import DiogoRangel.Bar.exception.ItemJaCadastrado;
import DiogoRangel.Bar.model.Cliente;
import DiogoRangel.Bar.repository.ClienteRepository;
import DiogoRangel.Bar.exception.DadosInvalidos;
import DiogoRangel.Bar.exception.ItemInexistente;
import DiogoRangel.Bar.exception.ItemJaCadastrado;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Injeção de dependências no construtor
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
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
}