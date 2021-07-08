package br.com.caelum.carangobom.config.seguranca;

import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioDto;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.openMocks;

class TokenServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        tokenService = new TokenService(authenticationManager);
        ReflectionTestUtils.setField(tokenService, "expiration", 1800000L);
    }

    @Test
    void deveGerarUmNovoToken() {
        AutenticacaoTokenDto tokenDto = getAutenticacaoTokenDto();
        Assertions.assertNotNull(tokenDto);
        Assertions.assertNotNull(tokenDto.getToken());
        Assertions.assertEquals("Bearer", tokenDto.getTipo());
        Assertions.assertTrue(tokenService.validaToken(tokenDto.getToken()));
    }

    @Test
    void deveRetornarFalseQuandoOTokenForInvalido() {
        AutenticacaoTokenDto tokenDto = new AutenticacaoTokenDto("", "Bearer");
        Assertions.assertFalse(tokenService.validaToken(tokenDto.getToken()));
    }

    @Test
    void deveRetornarOIdDoUsuario() {
        AutenticacaoTokenDto tokenDto = getAutenticacaoTokenDto();
        Long idDoUsuario = tokenService.getIdUsuario(tokenDto.getToken());
        Assertions.assertNotNull(idDoUsuario);
        Assertions.assertEquals(1L, idDoUsuario);
    }

    @Test
    void deveRetornarExcecaoComTokenInvalido() {
        Exception exception = assertThrows(MalformedJwtException.class, () -> {
            tokenService.getIdUsuario("wer523.wtgsf.dg");
        });

        Assertions.assertEquals("Unable to read JSON value: ����", exception.getMessage());
    }

    @Test
    void deveRecuperarUmTokenDoServletRequest() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        String token = getAutenticacaoTokenDto().getToken();
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        String tokenDoRequest = tokenService.recuperarToken(httpServletRequest);
        Assertions.assertEquals(token, tokenDoRequest);
    }

    @Test
    void deveRetornarNullQuandoNaoHouverTokenNoServletRequest() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("token.de.teste");

        String tokenDoRequest = tokenService.recuperarToken(httpServletRequest);
        Assertions.assertNull(tokenDoRequest);
    }

    @Test
    void deveRecuperarIdUsuario() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        String token = getAutenticacaoTokenDto().getToken();
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Long idUsuario = tokenService.recuperarIdUsuario(httpServletRequest);
        Assertions.assertEquals(1L, idUsuario);
    }

    @Test
    void deveRetornarNullCasoNaoTenhaToken() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("token.de.teste");

        Long idUsuario = tokenService.recuperarIdUsuario(httpServletRequest);
        Assertions.assertNull(idUsuario);
    }

    private AutenticacaoTokenDto getAutenticacaoTokenDto() {
        UsuarioDto usuarioDto = new UsuarioDto(1L, "maria.silva", "coxinha123");
        Usuario usuario = new Usuario(1L, "maria.silva", "coxinha123", null);
        var authentication = Mockito.mock(Authentication.class);

        var dadosLogin = new UsernamePasswordAuthenticationToken(usuarioDto.getNome(), usuarioDto.getSenha());
        Mockito.when(authenticationManager.authenticate(dadosLogin)).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(usuario);

        return tokenService.gerarToken(usuarioDto);
    }

}