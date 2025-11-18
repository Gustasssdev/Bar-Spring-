package DiogoRangel.Bar.model;

import DiogoRangel.Bar.classes.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;

    public Cliente() {}

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
}

/*
O cliente tem que ver as informações da conta da mesa
ou seja se tiver em uma mesa 3 contas ele dever ver as informações dessas contas, em sequencia
 */