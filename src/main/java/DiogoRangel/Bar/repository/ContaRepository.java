package DiogoRangel.Bar.repository;

import DiogoRangel.Bar.classes.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    @Query("SELECT SUM(p) FROM Conta c JOIN c.pagamentos p " +
            "WHERE c.dataFechamento BETWEEN :inicio AND :fim " +
            "AND c.estaAberta = FALSE")
    Optional<Double> sumTotalPagoByDataFechamentoBetween(LocalDateTime inicio, LocalDateTime fim);
}
