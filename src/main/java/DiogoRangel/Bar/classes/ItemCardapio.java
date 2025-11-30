package DiogoRangel.Bar.classes;

import DiogoRangel.Bar.enums.TipoItem;
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
    @Enumerated(EnumType.STRING)
    private TipoItem tipo;
    public ItemCardapio() {} // construtor JPA obrigatório

    public ItemCardapio(String nome, double valor, TipoItem tipo) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }

}