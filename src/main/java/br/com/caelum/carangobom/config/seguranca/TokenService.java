package br.com.caelum.carangobom.config.seguranca;

import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TokenService {

    private static final Logger logger = Logger.getLogger(TokenService.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${carangobom.jwt.expiration}")
    private Long expiration;

    @Value("${carangobom.jwt.secret}")
    private String secret;

    /**
     * Gera um token a partir de uma autenticação de usuário
     *
     * @param authentication o usuário autenticado
     * @return o token gerado
     */
    private String gerarToken(Authentication authentication) {
        var usuario = (Usuario) authentication.getPrincipal();
        var hoje = new Date();
        var expiracao = new Date(hoje.getTime() + expiration);
        return Jwts.builder().setIssuer("Carango Bom").setSubject(usuario.getId().toString()).setIssuedAt(hoje).setExpiration(expiracao).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * Cria uma autenticação a partir do usuário dado e gera um token
     *
     * @param usuarioDto o usuário a ser autenticado
     * @return o token
     */
    public AutenticacaoTokenDto gerarToken(UsuarioDto usuarioDto) {
        var dadosLogin = new UsernamePasswordAuthenticationToken(usuarioDto.getNome(), usuarioDto.getSenha());
        var authentication = authenticationManager.authenticate(dadosLogin);
        String token = gerarToken(authentication);
        return new AutenticacaoTokenDto(token, "Bearer");
    }

    /**
     * Valida o token dado
     *
     * @param token o token a ser validado
     * @return verdadeiro se o token for válido, falso se não for
     */
    public boolean validaToken(String token) {
        var result = false;
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            result = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return result;
    }

    /**
     * Recupera o id do usuário presente no token
     *
     * @param token o token a ter o id do usuário recuperado
     * @return o id do usuário
     */
    public Long getIdUsuario(String token) {
        var claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
