package DiogoRangel.Bar;

import DiogoRangel.Bar.enums.Perfil;
import DiogoRangel.Bar.model.Configuracao;
import DiogoRangel.Bar.model.Usuario;
import DiogoRangel.Bar.repository.ConfiguracaoRepository;
import DiogoRangel.Bar.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot. SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework. context.annotation.ComponentScan;
import org.springframework.data. jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure. domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "DiogoRangel.Bar")
@EnableJpaRepositories(basePackages = "DiogoRangel.Bar.repository")
@EntityScan(basePackages = {"DiogoRangel.Bar.model", "DiogoRangel.Bar.classes"})
public class BarApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarApplication. class, args);
    }
    /**
     * Garante que o registro de Configuração (ID 1L) exista.
     */
    @Bean
    public CommandLineRunner initConfiguracao(ConfiguracaoRepository configRepository) {
        return args -> {
            if (configRepository.findById(1L).isEmpty()) {
                Configuracao config = new Configuracao();
                // O ID 1L é definido no model Configuracao
                configRepository.save(config);
                System.out.println(">>> Configuração inicial do sistema criada com sucesso (ID: 1L) <<<");
            }
        };
    }
    @Bean
    public CommandLineRunner initUsuarios(UsuarioRepository usuarioRepository) {
        return args -> {
            // Criar Administrador
            if (usuarioRepository.findByUsername("admin") == null) {
                Usuario admin = new Usuario("admin", "admin123", Perfil.ADMINISTRADOR);
                admin.setNomeCompleto("Administrador do Sistema");
                usuarioRepository.save(admin);
                System.out.println(">>> ✅ Administrador criado: username=admin, senha=admin123 <<<");
            }

            // Criar Garçom
            if (usuarioRepository.findByUsername("garcom") == null) {
                Usuario garcom = new Usuario("garcom", "garcom123", Perfil.GARCOM);
                garcom.setNomeCompleto("Garçom Padrão");
                usuarioRepository.save(garcom);
                System.out.println(">>> ✅ Garçom criado: username=garcom, senha=garcom123 <<<");
            }
        };
    }
}