package curso.springboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@Configuration //classe de configuração spring
@EnableWebSecurity //configuração de segurança
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Override //configura as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {
	  
		http.csrf()
	   .disable() //Desativa as configurações de memoria do spring.
	   .authorizeRequests() //permiti restringir acessos
	   .antMatchers(HttpMethod.GET, "/").permitAll() //qualquer usuário acessa á página inicial
	   .anyRequest().authenticated()
	   .and().formLogin().permitAll() //formulario de login permite qlq usuario
	   .and().logout() //mapeia url delogout e invalida usuario autenticado
	   .logoutRequestMatcher(new  AntPathRequestMatcher("/logout"));//vai invalidar sessão /logout

	}

	@Override //Cria Autenticação do Usuário com banco de dados em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("admin") //usuario que eu quero
		.password("$2a$10$/bakAy1Gm5EKtCOTMNWxg.jppKIYV.sp659l5NqWHw66OWxyOYxLm")
		.roles("ADMIN");
	}
	
	@Override //ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		//deixa liberado a pasta que dá o css js pro nosso sistema
		web.ignoring().antMatchers("/materialize/**");
	}
}
