package DiogoRangel.Bar.classes;

import DiogoRangel.Bar.enums.TipoItem;
import DiogoRangel.Bar.model.Cliente;
import DiogoRangel.Bar.classes.ItemCardapio;
import DiogoRangel.Bar.model.Mesa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numConta;

    @ManyToOne
    private Cliente cliente;

    private boolean estaAberta = true;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consumo> consumos = new ArrayList<>();

    @ElementCollection
    private List<Double> pagamentos = new ArrayList<>();

    @ManyToOne
    private Mesa mesa; // Assumimos que Mesa tem o numPessoas

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now(); // Assume que já existe

    @Column(name = "data_fechamento") // NOVO CAMPO
    private LocalDateTime dataFechamento;

    public Conta() {}

    public Conta(int numConta, Cliente cliente, Mesa mesa) {
        this.numConta = numConta;
        this.cliente = cliente;
        this.mesa = mesa;
        this.estaAberta = true;
    }

    public double calcularGorjeta(double percBebida, double percComida) {
        double gorjeta = 0.0;
        for (Consumo consumo : consumos) {
            ItemCardapio item = consumo.getItem();

            if (item != null) {
                if (item.getTipo() == TipoItem.BEBIDA) {             // bebida
                    gorjeta += consumo.getValorTotal() * percBebida;
                } else if (item.getTipo() == TipoItem.COMIDA) {      // comida
                    gorjeta += consumo.getValorTotal() * percComida;
                }
            }
        }
        return gorjeta;
    }

    public double calcularTotalConsumido() {
        return consumos.stream()
                .mapToDouble(Consumo::getValorTotal)
                .sum();
    }

    public double getTotalPago() {
        return pagamentos.stream().mapToDouble(Double::doubleValue).sum();
    }

    public void fecharConta() {
        this.estaAberta = false;
    }
}