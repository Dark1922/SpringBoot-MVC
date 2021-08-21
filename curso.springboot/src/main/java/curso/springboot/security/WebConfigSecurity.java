package curso.springboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.AllArgsConstructor;

@Configuration //classe de configuração spring
@EnableWebSecurity //configuração de segurança
@AllArgsConstructor
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	
	@Override //configura as solicitações de acesso por http
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf()
	   .disable() //Desativa as configurações de memoria do spring.
	   .authorizeRequests() //permiti restringir acessos
	   .antMatchers(HttpMethod.GET, "/").permitAll() //qualquer usuário acessa á página inicial
	   .antMatchers(HttpMethod.GET, "/materialize/**").permitAll()
	   .antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN") 
	   .anyRequest().authenticated()
	   .and().formLogin().permitAll() //formulario de login permite qlq usuario
	   .loginPage("/login") //página de login
	   .defaultSuccessUrl("/cadastropessoa") //se logar manda ele pra essa página
	   .failureUrl("/login?error=true") //caso falhe o login	
	   .and()
	   .logout().logoutSuccessUrl("/login") //mapeia url delogout e invalida usuario autenticado deslogar vai pro /login
	   .logoutRequestMatcher(new  AntPathRequestMatcher("/logout"));//vai invalidar sessão /logout

	}

	@Override //Cria Autenticação do Usuário com banco de dados em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		
		//vai chamar automaticamente o loginusername e fazer todo o processo pra gente
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
		/*
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("admin") //usuario que eu quero
		.password("$2a$10$/bakAy1Gm5EKtCOTMNWxg.jppKIYV.sp659l5NqWHw66OWxyOYxLm")
		.roles("ADMIN");
		*/
	}
	
	@Override //ignora URL especificas
	public void configure(WebSecurity web) throws Exception {
		//deixa liberado a pasta que dá o css js pro nosso sistema
		 web.ignoring().antMatchers("/materialize/**")

         .antMatchers(HttpMethod.GET,"/resources/**","/static/**", "/**","/materialize/**", "**/materialize/**");
	}
}
