package DiogoRangel.Bar.dto;

import DiogoRangel.Bar.classes.Conta;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

public class ContaDetalhesDTO {
    private String tokenMesa;
    private Long contaId;
    private List<ConsumoDetalheDTO> itens;
    private double totalItens;
    private double valorGorjeta;
    private double valorCouvert;
    private double totalPago;
    private double valorPendente;
    private double valorTotalFinal;
    private boolean estaAberta;

    // De acordo com a classe ClienteService -- Verifica ela lá, gustadev
    public ContaDetalhesDTO(Conta conta, double valorTotalFinal, double valorPendente, double valorGorjeta, double valorCouvert) {
        this.tokenMesa = conta.getMesa().getToken();
        this.contaId = conta.getId();

        this.itens = conta.getConsumos().stream()
                .map(ConsumoDetalheDTO::new) // Mapeia para o DTO de detalhe
                .collect(Collectors.toList());

        this.totalItens = conta.calcularTotalConsumido();
        this.valorGorjeta = valorGorjeta;
        this.valorCouvert = valorCouvert;
        this.totalPago = conta.getTotalPago();
        this.valorPendente = valorPendente;
        this.valorTotalFinal = valorTotalFinal;
        this.estaAberta = conta.isEstaAberta();
    }
}