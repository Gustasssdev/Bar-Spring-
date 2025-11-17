package DiogoRangel.Bar.classes;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
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
        return item.getValor() * quantidade;
    }

}
