package DiogoRangel.Bar.repository;

import DiogoRangel.Bar.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por CPF
    Optional<Cliente> findByCpf(String cpf);

    // Verificar se existe cliente com o CPF
    boolean existsByCpf(String cpf);

    // Buscar cliente por nome
    Optional<Cliente> findByNome(String nome);

    // Verificar se existe cliente com o nome
    boolean existsByNome(String nome);
}