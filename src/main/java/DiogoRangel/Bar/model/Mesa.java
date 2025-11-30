
package DiogoRangel.Bar.model;

import DiogoRangel.Bar.classes.Conta;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;
    private int numPessoas;

    private String token;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Conta> contas = new ArrayList<>();

    private double couvert = 0;

    private boolean contaAtiva = true;

    public Mesa() {}

    public Mesa(int numero, String token, int numPessoas) {
        this.numero = numero;
        this.token = token;
        this.numPessoas = numPessoas;
        this.contas = new ArrayList<>();
    }
}
