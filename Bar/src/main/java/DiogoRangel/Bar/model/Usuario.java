package DiogoRangel.Bar.model;

import DiogoRangel.Bar.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "usuarios") // Nome da tabela
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Mapeamento do perfil (ADMIN, GARCOM, CLIENTE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    // diferenciar clientes cadastrados e contas abertas
    private String nomeCompleto;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cliente cliente;

    public Usuario() {}

    public Usuario(String username, String password, Perfil perfil) {
        this.username = username;
        this.password = password;
        this.perfil = perfil;
    }
}