package curso.springboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration //classe de configuração spring
@EnableWebSecurity //configuração de segurança
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Override //configura as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {
	   

	}

}
