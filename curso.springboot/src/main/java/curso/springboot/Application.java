package curso.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "curso.springboot.model")
@ComponentScan(basePackages = {"curso.*"})//mapea todos pacote que começa com curso
@EnableJpaRepositories(basePackages = {"curso.springboot.repository"})//caso ele n conheça o repository
@EnableTransactionManagement //parte da transação
public class Application {

	public static void main(String[] args) { 
		SpringApplication.run(Application.class, args);
	}

}
