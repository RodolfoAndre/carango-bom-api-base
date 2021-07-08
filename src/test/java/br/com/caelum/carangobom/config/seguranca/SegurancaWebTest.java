package br.com.caelum.carangobom.config.seguranca;

import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioDto;
import br.com.caelum.carangobom.usuario.UsuarioRepository;
import br.com.caelum.carangobom.veiculo.VeiculoController;
import br.com.caelum.carangobom.veiculo.VeiculoService;
import io.jsonwebtoken.Jwts;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SegurancaWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void preSetup() {
        openMocks(this);

        Usuario usuarioLogado = new Usuario(1L, "admin", "admin", null);
        usuarioRepository.save(usuarioLogado);
    }

    private String getToken() {
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        Optional<Usuario> usuarioLogado = usuarioRepository.findById(1L);
        Mockito.when(authentication.getPrincipal()).thenReturn(usuarioLogado.orElse(null));

        UsuarioDto usuarioDto = new UsuarioDto(1L, "admin", "admin");
        ReflectionTestUtils.setField(tokenService, "expiration", 1800000L);
        ReflectionTestUtils.setField(tokenService, "authenticationManager", authenticationManager);

        var tokenDto = tokenService.gerarToken(usuarioDto);
        return tokenDto.getTipo()+ " " + tokenDto.getToken();
    }

    @Test
    void deveRetornar200ParaEndpointsPublicosSemToken() throws Exception {
        var mvcResult = this.mockMvc.perform(get("/veiculos").header(HttpHeaders.AUTHORIZATION, ""))
                .andDo(print()).andReturn();

        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        Assertions.assertEquals(HttpStatus.SC_OK, mvcResult.getResponse().getStatus());
    }

    @Test
    void deveRetornar200ParaEndpointsPrivadosComToken() throws Exception {
        var mvcResult = this.mockMvc.perform(get("/marcas").header(HttpHeaders.AUTHORIZATION, getToken()))
                .andDo(print()).andReturn();

        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        Assertions.assertEquals(HttpStatus.SC_OK, mvcResult.getResponse().getStatus());
    }

}