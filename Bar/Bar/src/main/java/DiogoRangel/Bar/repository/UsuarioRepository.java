package DiogoRangel.Bar.repository;

import DiogoRangel.Bar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByToken(String token);
}