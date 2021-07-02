package br.com.caelum.carangobom.config.seguranca;

import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioRepository;
import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Classe responsável pelo filtro de autenticação por token
 */
public class AutenticacaoTokenFiltro extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoTokenFiltro(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(httpServletRequest);
        var valido = tokenService.validaToken(token);
        if (valido) {
            autenticarCliente(token);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * Autentica o cliente através do token
     *
     * @param token o token do cliente
     */
    private void autenticarCliente(String token) {
        var usuarioId = tokenService.getIdUsuario(token);
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        var usuario = usuarioOptional.orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /**
     * Recupera o token pelo request
     *
     * @param httpServletRequest a requisição contendo o token
     * @return o token, caso seja encontrado
     */
    private String recuperarToken(HttpServletRequest httpServletRequest) {
        String result = null;
        var token = httpServletRequest.getHeader("Authorization");
        if (!token.isBlank() && token.startsWith("Bearer ")) {
            result = token.substring(7);
        }
        return result;
    }
}
