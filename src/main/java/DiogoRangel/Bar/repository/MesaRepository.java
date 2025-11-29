package DiogoRangel.Bar.repository;

import DiogoRangel.Bar.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByTokenAndContaAtivaIsTrue(String token);

}

