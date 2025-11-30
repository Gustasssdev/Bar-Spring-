package DiogoRangel.Bar.dto;

import DiogoRangel.Bar.classes.Consumo;
import lombok.Getter;
import lombok.Setter;


 @Getter  @Setter
public class ConsumoDetalheDTO {
    private String nomeItem;
    private String tipo;
    private int quantidade;
    private double precoUnitario;
    private double subtotal;
    private boolean cancelado;

     public ConsumoDetalheDTO(Consumo consumo) {
         this.nomeItem = consumo.getItem().getNome();

         this.tipo = consumo.getItem().getTipo().toString();

         this.quantidade = consumo.getQuantidade();

         // CORREÇÃO 2: A entidade ItemCardapio agora tem o campo 'preco', não 'valor'
         this.precoUnitario = consumo.getItem().getValor();

         this.subtotal = this.quantidade * this.precoUnitario;
         this.cancelado = consumo.isCancelado();
     }
}