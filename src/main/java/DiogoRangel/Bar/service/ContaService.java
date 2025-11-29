package DiogoRangel.Bar.service;

import DiogoRangel.Bar.classes.Conta;
import DiogoRangel.Bar.model.Configuracao;
import DiogoRangel.Bar.repository.ConfiguracaoRepository;
import DiogoRangel.Bar.repository.ContaRepository;
import DiogoRangel.Bar.exception.ContaInexistente;
import DiogoRangel.Bar.exception.PagamentoMaior;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ContaService {

    private final ContaRepository contaRepository;
    private final ConfiguracaoRepository configuracaoRepository;

    public ContaService(ContaRepository contaRepository, ConfiguracaoRepository configuracaoRepository) {
        this.contaRepository = contaRepository;
        this.configuracaoRepository = configuracaoRepository;
    }

    public double calcularTotalDaConta(Long contaId, int numeroPessoasMesa) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        // 1. Busca a Configuração Dinâmica
        Configuracao config = configuracaoRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Configuração do sistema não encontrada."));

        // 2. Calcula as partes
        double totalItens = conta.calcularTotalConsumido();
        double gorjeta = conta.calcularGorjeta(
                config.getPercentualGorjetaBebida(),
                config.getPercentualGorjetaComida()
        );
        double couvert = config.getPrecoCouvertPorPessoa() * numeroPessoasMesa;

        // 3. Retorna o valor total
        return totalItens + gorjeta + couvert;
    }

    public double calcularValorPendente(Long contaId, int numeroPessoasMesa) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        double valorTotalConta = calcularTotalDaConta(contaId, numeroPessoasMesa);
        double totalPago = conta.getTotalPago();

        // Regra de Negócio: Pagamento não pode ser maior que a conta
        if (totalPago > valorTotalConta) {
            throw new PagamentoMaior("O pagamento (R$" + totalPago + ") excede o valor total da conta (R$" + valorTotalConta + ")");
        }

        return valorTotalConta - totalPago;
    }

    public Conta fecharConta(Long contaId, int numeroPessoasMesa, Long garcomId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ContaInexistente("Conta não encontrada."));

        if (!conta.isEstaAberta()) {
            return conta; // Já está fechada
        }

        double valorPendente = calcularValorPendente(contaId, numeroPessoasMesa);

        if (valorPendente > 0) {
            throw new IllegalStateException("A conta não pode ser fechada. Valor pendente: R$" + valorPendente);
        }

        conta.fecharConta();
        conta.setDataFechamento(LocalDateTime.now()); //Define a data de fechamento
        return contaRepository.save(conta);
    }
}