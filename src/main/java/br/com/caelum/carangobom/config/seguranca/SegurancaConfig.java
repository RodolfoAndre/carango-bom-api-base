package br.com.caelum.carangobom.config.seguranca;

import br.com.caelum.carangobom.autenticacao.AutenticacaoService;
import br.com.caelum.carangobom.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SegurancaConfig extends WebSecurityConfigurerAdapter {

	private AutenticacaoService autenticacaoService;

	private TokenService tokenService;

	private UsuarioRepository usuarioRepository;

	@Autowired
	public SegurancaConfig(AutenticacaoService autenticacaoService, TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.autenticacaoService = autenticacaoService;
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	//Configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configuracoes de autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/auth").permitAll().anyRequest().authenticated().and().csrf()
				.disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(new AutenticacaoTokenFiltro(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	//Configuracoes de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
		// Já que não temos js, css, imagens e outros assets, não será implementado
	}
	
}