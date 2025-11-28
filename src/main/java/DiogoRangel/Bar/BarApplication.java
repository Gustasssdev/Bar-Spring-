package DiogoRangel.Bar;

import org.springframework.boot. SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}