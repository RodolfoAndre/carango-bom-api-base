package br.com.caelum.carangobom.autenticacao;

import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByNome(email);
		return usuario.orElseThrow(() -> new UsernameNotFoundException("Dados inválidos!"));
	}
}
