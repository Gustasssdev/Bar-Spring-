package DiogoRangel.Bar.dto;

import DiogoRangel.Bar.classes.Consumo;
import DiogoRangel.Bar.enums.StatusConsumo;
import lombok.Getter;
import lombok.Setter;


 @Getter  @Setter
public class ConsumoDetalheDTO {
    private String nomeItem;
    private int tipo;
    private int quantidade;
    private double precoUnitario;
    private double subtotal;
    private boolean cancelado;

    public ConsumoDetalheDTO(Consumo consumo) {
        this.nomeItem = consumo.getItem().getNome();
        this.tipo = (consumo.getItem().getTipo());
        this.quantidade = consumo.getQuantidade();
        this.precoUnitario = consumo.getItem().getValor();
        this.subtotal = this.quantidade * this.precoUnitario;
        this.cancelado = consumo.isCancelado();
    }
}