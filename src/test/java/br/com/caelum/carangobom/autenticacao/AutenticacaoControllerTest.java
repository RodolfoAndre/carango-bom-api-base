package br.com.caelum.carangobom.autenticacao;

import br.com.caelum.carangobom.config.seguranca.AutenticacaoTokenDto;
import br.com.caelum.carangobom.config.seguranca.TokenService;
import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.usuario.UsuarioDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AutenticacaoControllerTest {

    @Mock
    private TokenService tokenService;

    private AutenticacaoController autenticacaoController;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        autenticacaoController = new AutenticacaoController(tokenService);
    }

    @Test
    void deveAutenticarUsuarioEhRetornarToken() {
        AutenticacaoTokenDto autenticacaoTokenDto = new AutenticacaoTokenDto(
                    "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDYXJhbmdvIEJvbSIsInN1YiI6IjEiLCJpYXQiOjE2MjU2NzcxNTMsImV4cCI6MTYyNTY3ODk1M30._mrPB9JPFVl4FdxkzBt9QIGot4jIQ_96HmLa9ZteM8I",
                    "Bearer");

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNome("Maria");
        usuarioDto.setSenha("senha123");

        when(tokenService.gerarToken(usuarioDto))
                .thenReturn(autenticacaoTokenDto);

        ResponseEntity<AutenticacaoTokenDto> resposta = autenticacaoController.autenticar(usuarioDto);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(autenticacaoTokenDto, resposta.getBody());
    }

    @Test
    void naoDeveAutenticarERetornarTokenSeNaoEncobtrarUsuario() {
        when(tokenService.gerarToken(any(UsuarioDto.class)))
                .thenThrow(new NotFoundException("Entidade não encontrada"));

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNome("Maria");
        usuarioDto.setSenha("senha123");

        ResponseEntity<AutenticacaoTokenDto> resposta = autenticacaoController.autenticar(usuarioDto);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}