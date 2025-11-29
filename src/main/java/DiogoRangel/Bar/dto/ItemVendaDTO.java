package DiogoRangel.Bar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO para representar um item no relatório
@Data
@AllArgsConstructor
public class ItemVendaDTO {
    private int tipo;
    private String nomeItem;
    private Long totalVendido; // Para 'Itens mais vendidos'
    private double faturamentoTotal; // Para 'Itens com maior faturamento'

    // Construtor específico para "Itens mais vendidos"
    public ItemVendaDTO(int tipo, String nomeItem, Long totalVendido) {
        this.tipo = tipo;
        this.nomeItem = nomeItem;
        this.totalVendido = totalVendido;
    }
}
