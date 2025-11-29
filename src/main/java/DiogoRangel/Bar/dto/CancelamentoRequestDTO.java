package DiogoRangel.Bar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CancelamentoRequestDTO {
    private Long consumoId;
    private String motivo;
    private Long garcomId;
}