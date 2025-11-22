package DiogoRangel.Bar.classes;

import DiogoRangel.Bar.exception.PagamentoMaior;
import DiogoRangel.Bar.model.Cliente;
import DiogoRangel.Bar.model.Mesa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne // Mapeamento reverso para a relação OneToMany na Mesa
    private Mesa mesa;

    public Conta() {}

    public Conta(int numConta, Cliente cliente, Mesa mesa) {
        this.numConta = numConta;
        this.cliente = cliente;
        this.mesa = mesa;
        this.estaAberta = true;
    }

  /*  public void adicionarPedido(ItemCardapio item, int quantidade) {
        if (!this.estaAberta)
            throw new RuntimeException("Conta fechada.");

        Consumo consumo = new Consumo(item, quantidade, this);
        consumos.add(consumo);
    }*/

    public double calcularGorjeta() {
        double gorjeta = 0.0;
        for (Consumo consumo : consumos) {
            ItemCardapio item = consumo.getItem();

            if (item.getTipo() == 2) {             // bebida
                gorjeta += consumo.getValorTotal() * 0.10;
            } else if (item.getTipo() == 3) {      // comida
                gorjeta += consumo.getValorTotal() * 0.15;
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

    public double getValorTotalDaConta() {
        return calcularTotalConsumido() + calcularGorjeta();
    }

    public double getValorPendente() throws PagamentoMaior{
        if (getTotalPago() > getValorTotalDaConta()){
            throw new PagamentoMaior("O pagamento é maior do que a conta");
        }
        return getValorTotalDaConta() - getTotalPago();
    }

    public void fecharConta() {
        if(getValorPendente() == 0){
            this.estaAberta = false;
        }
    }
}
