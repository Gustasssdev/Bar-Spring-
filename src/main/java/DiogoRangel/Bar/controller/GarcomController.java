package DiogoRangel.Bar.controller;

import DiogoRangel.Bar.dto.AberturaMesaDTO;
import DiogoRangel.Bar.dto.ConsumoRequestDTO;
import DiogoRangel.Bar.dto.PagamentoRequestDTO;
import DiogoRangel.Bar.dto.CancelamentoRequestDTO;

import DiogoRangel.Bar.model.Mesa;
import DiogoRangel.Bar.classes.Consumo;
import DiogoRangel.Bar.classes.Conta;
import DiogoRangel.Bar.service.GarcomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Se estiver usando validação

@RestController
@RequestMapping("/api/garcom")
public class GarcomController {

    private final GarcomService garcomService;

    public GarcomController(GarcomService garcomService) {
        this.garcomService = garcomService;
    }

    // 1. ABRIR MESA (POST)
    // EX: POST /api/garcom/abrir
    @PostMapping("/abrir")
    public ResponseEntity<Mesa> abrirMesa(@RequestBody @Valid AberturaMesaDTO dto) {
        Mesa novaMesa = garcomService.abrirMesa(
                dto.getNumeroMesa(),
                dto.getNumeroPessoas(),
                dto.getGarcomId(),
                dto.getClienteId()
        );
        return new ResponseEntity<>(novaMesa, HttpStatus.CREATED);
    }

    // 2. ADICIONAR ITEM (POST)
    // EX: POST /api/garcom/consumo/adicionar
    @PostMapping("/consumo/adicionar")
    public ResponseEntity<Consumo> adicionarConsumo(@RequestBody @Valid ConsumoRequestDTO dto) {
        Consumo novoConsumo = garcomService.adicionarItem( // Use adicionarItem conforme seu GarcomService
                dto.getContaId(),
                dto.getItemCardapioId(),
                dto.getQuantidade(),
                dto.getGarcomId()
        );
        return new ResponseEntity<>(novoConsumo, HttpStatus.CREATED);
    }

    // 3. CANCELAR CONSUMO (PUT/PATCH, mas usaremos POST para simplicidade com body)
    // EX: POST /api/garcom/consumo/cancelar
    @PostMapping("/consumo/cancelar")
    public ResponseEntity<Void> cancelarConsumo(@RequestBody @Valid CancelamentoRequestDTO dto) {
        garcomService.cancelarConsumo(
                dto.getConsumoId(),
                dto.getMotivo(),
                dto.getGarcomId()
        );
        return ResponseEntity.noContent().build();
    }

    // 4. REGISTRAR PAGAMENTO (POST)
    // EX: POST /api/garcom/pagamento
    @PostMapping("/pagamento")
    public ResponseEntity<Conta> registrarPagamento(@RequestBody @Valid PagamentoRequestDTO dto) {
        Conta contaAtualizada = garcomService.registrarPagamento(
                dto.getContaId(),
                dto.getValorPago(),
                dto.getGarcomId()
        );
        return ResponseEntity.ok(contaAtualizada);
    }

    // 5. FECHAR CONTA (PUT)
    // EX: PUT /api/garcom/fechar/{contaId}/{garcomId}
    @PutMapping("/fechar/{contaId}/{garcomId}")
    public ResponseEntity<Conta> fecharConta(@PathVariable Long contaId, @PathVariable Long garcomId) {
        Conta contaFechada = garcomService.fecharConta(contaId, garcomId);
        return ResponseEntity.ok(contaFechada);
    }
}