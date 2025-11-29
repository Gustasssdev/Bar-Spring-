package DiogoRangel.Bar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoRequestDTO {
    private Long contaId;
    private double valorPago;
    private Long garcomId; // Quem está registrando
}