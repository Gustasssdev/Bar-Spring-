package DiogoRangel.Bar;

import org.springframework.boot.SpringApplication;
import org. springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "DiogoRangel.Bar")  // ✅ Escaneia todos os pacotes
@EnableJpaRepositories(basePackages = "DiogoRangel.Bar.repository")  // ✅ Encontra os Repositories
@EntityScan(basePackages = "DiogoRangel.Bar.model")  // ✅ Encontra as Entidades
public class BarApplication {

    public static void main(String[] args) {
        SpringApplication. run(BarApplication.class, args);
    }
}