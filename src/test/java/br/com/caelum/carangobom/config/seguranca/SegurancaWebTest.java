package br.com.caelum.carangobom.config.seguranca;

import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioDto;
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

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
    }

    private String getToken() {

        Usuario usuarioLogado = new Usuario(1L, "admin", "adminz", null);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(usuarioLogado);

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

        Assertions.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
        Assertions.assertEquals(HttpStatus.SC_OK,
                mvcResult.getResponse().getStatus());
    }

    @Test
    void deveRetornar200ParaEndpointsPrivadosComToken() throws Exception {
        var mvcResult = this.mockMvc.perform(get("/marcas").header(HttpHeaders.AUTHORIZATION, getToken()))
                .andDo(print()).andReturn();

        Assertions.assertEquals("application/json", mvcResult.getResponse().getContentType());
        Assertions.assertEquals(HttpStatus.SC_OK, mvcResult.getResponse().getStatus());
    }

}