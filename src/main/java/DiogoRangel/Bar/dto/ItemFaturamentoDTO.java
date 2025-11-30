package DiogoRangel.Bar.dto;

import DiogoRangel.Bar.enums.TipoItem;
import lombok.Getter;

@Getter
public class ItemFaturamentoDTO {

    private TipoItem tipo;
    private String nome;
    private final Double valorFaturado;

    public ItemFaturamentoDTO(TipoItem tipo, String nome, Double faturamentoTotal) {
        this.tipo = tipo;
        this.nome = nome;
        this.valorFaturado = faturamentoTotal;
    }

}
