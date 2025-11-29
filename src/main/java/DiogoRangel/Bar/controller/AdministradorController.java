package DiogoRangel.Bar.controller;

import DiogoRangel.Bar.dto.ConfiguracaoRequestDTO;
import DiogoRangel.Bar.dto.MesaRequestDTO;
import DiogoRangel.Bar.dto.ItemCardapioRequestDTO;
import DiogoRangel.Bar.model.Configuracao;
import DiogoRangel.Bar.model.Mesa;
import DiogoRangel.Bar.classes.ItemCardapio;
import DiogoRangel.Bar.service.AdministradorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin") // Endpoint base para o Administrador
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }


     //PUT /api/admin/configuracao
      //Atualiza as regras de negócio (couvert, gorjetas).

    @PutMapping("/configuracao")
    public ResponseEntity<Configuracao> atualizarConfiguracao(@RequestBody @Valid ConfiguracaoRequestDTO dto) {
        Configuracao config = administradorService.atualizarRegras(
                dto.getPrecoCouvertPorPessoa(),
                dto.getPercentualGorjetaBebida(),
                dto.getPercentualGorjetaComida()
        );
        return ResponseEntity.ok(config);
    }

     //POST /api/admin/mesas
     //Cadastrar  nova mesa.

    @PostMapping("/mesas")
    public ResponseEntity<Mesa> cadastrarMesa(@RequestBody @Valid MesaRequestDTO dto) {
        // Cria um objeto Mesa a partir do DTO de Request
        Mesa novaMesa = new Mesa(dto.getNumero(), null, 0);
        novaMesa.setContaAtiva(false); // Uma mesa cadastrada começa inativa

        Mesa mesaSalva = administradorService.cadastrarMesa(novaMesa);
        return new ResponseEntity<>(mesaSalva, HttpStatus.CREATED);
    }
      //POST /api/admin/cardapio
      //Cadastra um novo item no cardápio.

    @PostMapping("/cardapio")
    public ResponseEntity<ItemCardapio> cadastrarItem(@RequestBody @Valid ItemCardapioRequestDTO dto) {
        // Cria um objeto ItemCardapio
        ItemCardapio novoItem = new ItemCardapio(dto.getNome(),dto.getPreco(), dto.getTipo());

        ItemCardapio itemSalvo = administradorService.cadastrarItem(novoItem);
        return new ResponseEntity<>(itemSalvo, HttpStatus.CREATED);
    }

    @GetMapping("/relatorio/faturamento")
    public ResponseEntity<Double> gerarRelatorioFaturamento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        double faturamento = administradorService.gerarRelatorioFaturamento(inicio, fim);
        return ResponseEntity.ok(faturamento);
    }

}