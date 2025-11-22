package DiogoRangel.Bar.classes;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ItemCardapio {

    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double valor;
    private int tipo;   // 2 = bebida, 3 = comida

    public ItemCardapio() {} // construtor JPA obrigatório

    public ItemCardapio(String nome, double valor, int tipo) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }

}