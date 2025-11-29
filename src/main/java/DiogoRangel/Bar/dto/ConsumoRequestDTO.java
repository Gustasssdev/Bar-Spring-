package DiogoRangel.Bar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ConsumoRequestDTO {
    private Long contaId;
    private Long itemCardapioId;
    private int quantidade;
    private Long garcomId; // Quem está registrando
}