package curso.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "curso.springboot.model")
@ComponentScan(basePackages = {"curso.*"})//mapea todos pacote que começa com curso
@EnableJpaRepositories(basePackages = {"curso.springboot.repository"})//caso ele n conheça o repository
@EnableTransactionManagement //parte da transação
@EnableWebMvc //recursos web mvc pra liberar pra nossa tela de login
public class Application implements WebMvcConfigurer {

	public static void main(String[] args) { 
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//estamos pegando a view da url que vem padrão e passando pra nossa que é pra seta
		registry.addViewController("/login").setViewName("/login");
		
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
		
	}

}
