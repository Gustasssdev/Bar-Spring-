package DiogoRangel.Bar.dto;

import DiogoRangel.Bar.enums.TipoItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCardapioRequestDTO {
    private String nome;
    private TipoItem tipo;
    private double preco;
}