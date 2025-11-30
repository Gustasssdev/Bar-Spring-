package DiogoRangel.Bar.dto;

import DiogoRangel.Bar.enums.TipoItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemVendaDTO {
    private TipoItem tipo;
    private String nome;
    private Long quantidadeVendida;
}
