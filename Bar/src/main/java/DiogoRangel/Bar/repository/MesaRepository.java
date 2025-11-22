package DiogoRangel.Bar.repository;

import DiogoRangel.Bar.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Mesa findByToken(String token);
}

