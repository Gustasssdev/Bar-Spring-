package DiogoRangel.Bar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Configuracao {

    @Id
    private Long id = 1L;

    private double percentualGorjetaBebida = 0.10;
    private double percentualGorjetaComida = 0.15;
    private double precoCouvertPorPessoa = 0.0;
}