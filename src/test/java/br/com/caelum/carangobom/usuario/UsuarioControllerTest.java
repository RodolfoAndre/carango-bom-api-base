package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.config.seguranca.TokenService;
import br.com.caelum.carangobom.exception.MensagensExcecoes;
import br.com.caelum.carangobom.exception.NotAllowedException;
import br.com.caelum.carangobom.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioControllerTest {

    public static final String MARIA_STRING_CONSTANT = "Maria";

    public static final String SENHA_ALTERADA_STRING_CONSTANT = "senhaalterada";

    public static final long ID_DEFAULT_LONG_MOCK = 1L;

    private UriComponentsBuilder uriBuilder;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TokenService tokenService;

    private UsuarioController usuarioController;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        usuarioController = new UsuarioController(usuarioService, tokenService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaDeUsariosQuandoHouverResultados() {
        List<UsuarioDto> usuarios = List.of(
                new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT),
                new UsuarioDto(2L, "João")
        );

        when(usuarioService.listar())
                .thenReturn(usuarios);

        ResponseEntity<List<UsuarioDto>> resultado = usuarioController.listarUsuarios();

        assertEquals(usuarios, resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test
    void deveRetornarUsuarioPeloId() {
        UsuarioDto usuario = new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT);

        when(usuarioService.obterPorId(ID_DEFAULT_LONG_MOCK))
                .thenReturn(usuario);

        ResponseEntity<UsuarioDto> resposta = usuarioController.obterUsuarioPorId(ID_DEFAULT_LONG_MOCK);
        assertEquals(usuario, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoTentarBuscarUusuarioComIdInexistente() {
        when(usuarioService.obterPorId(anyLong()))
                .thenThrow(new NotFoundException(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<UsuarioDto> resposta = usuarioController.obterUsuarioPorId(ID_DEFAULT_LONG_MOCK);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarUsuario() {
        UsuarioDto usuarioDto = new UsuarioDto(null, MARIA_STRING_CONSTANT, "senhateste");

        when(usuarioService.cadastrarUsuario(usuarioDto))
                .thenReturn(new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT));

        ResponseEntity<UsuarioDto> resposta = usuarioController.cadastrarUsuario(usuarioDto, uriBuilder);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("http://localhost:8080/usuarios/1", resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarSenhaUsuario() {
        UsuarioDto usuarioDto = new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT);
        UsuarioDto usuarioDtoAlterado = new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT, SENHA_ALTERADA_STRING_CONSTANT);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);

        when(usuarioService.alterarUsuario(ID_DEFAULT_LONG_MOCK, usuarioDto, ID_DEFAULT_LONG_MOCK)).thenReturn(usuarioDtoAlterado);
        when(tokenService.recuperarIdUsuario(requestMock)).thenReturn(ID_DEFAULT_LONG_MOCK);

        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarUsuario(ID_DEFAULT_LONG_MOCK, usuarioDto, requestMock);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        UsuarioDto usuarioAlterado = resposta.getBody();
        assertNotNull(usuarioAlterado);
        assertEquals(SENHA_ALTERADA_STRING_CONSTANT, usuarioAlterado.getSenha());
    }

    @Test
    void naoDeveAlterarUsuarioInexistente() {
        when(usuarioService.alterarUsuario(Mockito.eq(ID_DEFAULT_LONG_MOCK), any(UsuarioDto.class), Mockito.eq(ID_DEFAULT_LONG_MOCK)))
                .thenThrow(new NotFoundException(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM));
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);

        when(tokenService.recuperarIdUsuario(requestMock)).thenReturn(ID_DEFAULT_LONG_MOCK);
        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarUsuario(ID_DEFAULT_LONG_MOCK, new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT, SENHA_ALTERADA_STRING_CONSTANT), requestMock);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void naoDeveAlterarUsuarioComIdDiferente() {
        when(usuarioService.alterarUsuario(Mockito.eq(ID_DEFAULT_LONG_MOCK), any(UsuarioDto.class), Mockito.eq(2L)))
                .thenThrow(new NotAllowedException(MensagensExcecoes.TROCAR_SENHA_DE_OUTRO_USUARIO_MENSAGEM));
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        UsuarioDto usuarioDto = new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT, SENHA_ALTERADA_STRING_CONSTANT);

        when(tokenService.recuperarIdUsuario(requestMock)).thenReturn(2L);
        Exception exception = assertThrows(NotAllowedException.class, () -> {
            usuarioController.alterarUsuario(ID_DEFAULT_LONG_MOCK, usuarioDto, requestMock);});

        assertEquals(MensagensExcecoes.TROCAR_SENHA_DE_OUTRO_USUARIO_MENSAGEM, exception.getMessage());
    }

    @Test
    void deveDeletarUsuarioExistente() {
        UsuarioDto usuario = new UsuarioDto(ID_DEFAULT_LONG_MOCK, MARIA_STRING_CONSTANT);

        when(usuarioService.obterPorId(ID_DEFAULT_LONG_MOCK))
                .thenReturn(usuario);

        ResponseEntity<UsuarioDto> resposta = usuarioController.deletarUsuario(ID_DEFAULT_LONG_MOCK);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(usuarioService).deletar(usuario.getId());
    }

    @Test
    void deveDarErroAoTentarDeletarUsuarioInexistente() {
        when(usuarioService.deletar(anyLong()))
                .thenThrow(new NotFoundException(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<UsuarioDto> resposta = usuarioController.deletarUsuario(ID_DEFAULT_LONG_MOCK);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}