package DiogoRangel.Bar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelarGorjetaResponseDTO {
    private Long contaId;
    private String mensagem;
    private double gorjetaRemovida;
    private double novoTotalConta;
    private boolean gorjetaCancelada;

    public CancelarGorjetaResponseDTO(Long contaId, String mensagem, double gorjetaRemovida, 
                                       double novoTotalConta, boolean gorjetaCancelada) {
        this.contaId = contaId;
        this.mensagem = mensagem;
        this.gorjetaRemovida = gorjetaRemovida;
        this.novoTotalConta = novoTotalConta;
        this.gorjetaCancelada = gorjetaCancelada;
    }
}
