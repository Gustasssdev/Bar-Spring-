package DiogoRangel.Bar;

import DiogoRangel.Bar.model.Configuracao;
import DiogoRangel.Bar.repository.ConfiguracaoRepository;
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
}