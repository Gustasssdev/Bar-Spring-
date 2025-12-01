package DiogoRangel.Bar.dto;

import lombok.Getter;

@Getter
public class CancelarGorjetaResponseDTO {
    private final Long contaId;
    private final String mensagem;
    private final double gorjetaRemovida;
    private final double novoTotalConta;
    private final boolean gorjetaCancelada;

    public CancelarGorjetaResponseDTO(Long contaId, String mensagem, double gorjetaRemovida, 
                                       double novoTotalConta, boolean gorjetaCancelada) {
        this.contaId = contaId;
        this.mensagem = mensagem;
        this.gorjetaRemovida = gorjetaRemovida;
        this.novoTotalConta = novoTotalConta;
        this.gorjetaCancelada = gorjetaCancelada;
    }
}
