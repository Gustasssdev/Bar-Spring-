package DiogoRangel.Bar.classes;

import DiogoRangel.Bar.enums.StatusConsumo;
import DiogoRangel.Bar.exception.DadosInvalidos;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Conta conta;

    @ManyToOne
    private ItemCardapio item;

    private int quantidade;

    private double valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusConsumo status = StatusConsumo.PEDIDO;

    private String motivoCancelamento;

    public Consumo(ItemCardapio item, int quantidade, Conta conta) {
        this.item = item;
        this.quantidade = quantidade;
        this.conta = conta;

        this.valorTotal = item.getValor() * quantidade;
        this.status = StatusConsumo.PEDIDO;
    }

    public void cancelar(String motivo) {
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new DadosInvalidos("O motivo do cancelamento é obrigatório.");
        }

        if (this.status == StatusConsumo.CANCELADO) {
            throw new IllegalStateException("Este consumo já está cancelado.");
        }

        this.status = StatusConsumo.CANCELADO;
        this.valorTotal = 0.0;
        this.motivoCancelamento = motivo;
    }

    public boolean isCancelado() {
        return this.status == StatusConsumo.CANCELADO;
    }
}
