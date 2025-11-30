package DiogoRangel.Bar. controller;

import DiogoRangel.Bar.dto.ClienteDTO;
import DiogoRangel.Bar.dto.ContaDetalhesDTO;
import DiogoRangel.Bar.model.Cliente;
import DiogoRangel.Bar.service.ClienteService;
import org.springframework.http.HttpStatus;
import org. springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Listar todos os clientes
    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService. listarTodos();
        return ResponseEntity.ok(clientes);
    }

    // Cadastrar novo cliente
    @PostMapping("/cadastrarcliente")
    public ResponseEntity<Cliente> cadastrar(@RequestBody ClienteDTO dto) {
        Cliente cliente = clienteService.cadastrar(dto. getNome(), dto.getCpf());
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    // Buscar por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarPorCpf(@PathVariable String cpf) {
        Cliente cliente = clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(cliente);
    }

    // Atualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        Cliente cliente = clienteService. atualizar(id, dto. getNome(), dto.getCpf());
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/consumo/{tokenMesa}")
    public ResponseEntity<ContaDetalhesDTO> consultarContaPorToken(@PathVariable String tokenMesa) {

        ContaDetalhesDTO detalhes = clienteService.consultarContaPorToken(tokenMesa);

        return ResponseEntity.ok(detalhes);
    }

    // Deletar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}