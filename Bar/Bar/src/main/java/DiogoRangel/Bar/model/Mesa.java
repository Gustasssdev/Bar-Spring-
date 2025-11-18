package DiogoRangel.Bar.model;

import DiogoRangel.Bar.classes.Conta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;

    private String token;

    @OneToOne(cascade = CascadeType.ALL)
    private Conta conta;

    private double couvert = 0;

    private boolean contaAtiva = true;

    public Mesa() {}

    public Mesa(int numero, String token, Conta conta) {
        this.numero = numero;
        this.token = token;
        this.conta = conta;
    }
}
