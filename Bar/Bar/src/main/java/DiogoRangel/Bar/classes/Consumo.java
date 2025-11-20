package DiogoRangel.Bar.classes;

import DiogoRangel.Bar.enums.StatusConsumo;
import DiogoRangel.Bar.exception.*; // Importe a sua exception
import DiogoRangel.Bar.classes.ItemCardapio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // Adicionado para permitir o Garçom manipular o status

@Entity
@Getter @Setter

public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    private Conta conta;

    @Getter
    @ManyToOne
    private ItemCardapio item;

    private int quantidade;

    public Consumo() {} // JPA exige

    public Consumo(ItemCardapio item, int quantidade, Conta conta) {
        this.item = item;
        this.quantidade = quantidade;
        this.conta = conta;
    }

    public double getValorTotal() {
        if (this.status == StatusConsumo.CANCELADO) {
            return 0.0;
        }
        // Utiliza o método getValor() que você já definiu
        return item.getValor() * quantidade;
    }

}
