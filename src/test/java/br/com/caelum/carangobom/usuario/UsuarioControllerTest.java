package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.MarcaDto;
import br.com.caelum.carangobom.veiculo.VeiculoController;
import br.com.caelum.carangobom.veiculo.VeiculoDto;
import br.com.caelum.carangobom.veiculo.VeiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioControllerTest {

    public static final String ENTIDADE_NAO_ENCONTRADO_MENSAGEM = "Entidade não encontrada";

    private UriComponentsBuilder uriBuilder;

    @Mock
    private UsuarioService usuarioService;

    private UsuarioController usuarioController;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        usuarioController = new UsuarioController(usuarioService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaDeUsariosQuandoHouverResultados() {
        List<UsuarioDto> usuarios = List.of(
                new UsuarioDto(1L, "Maria"),
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
        UsuarioDto usuario = new UsuarioDto(1L, "Maria");

        when(usuarioService.obterPorId(1L))
                .thenReturn(usuario);

        ResponseEntity<UsuarioDto> resposta = usuarioController.obterUsuarioPorId(1L);
        assertEquals(usuario, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoTentarBuscarUusuarioComIdInexistente() {
        when(usuarioService.obterPorId(anyLong()))
                .thenThrow(new NotFoundException(ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<UsuarioDto> resposta = usuarioController.obterUsuarioPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarUsuario() {
        UsuarioDto usuarioDto = new UsuarioDto(null, "Maria", "senhateste");

        when(usuarioService.cadastrarUsuario(usuarioDto))
                .thenReturn(new UsuarioDto(1L, "Maria"));

        ResponseEntity<UsuarioDto> resposta = usuarioController.cadastrarUsuario(usuarioDto, uriBuilder);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("http://localhost:8080/usuarios/1", resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarSenhaUsuario() {
        UsuarioDto usuarioDto = new UsuarioDto(1L, "Maria");
        UsuarioDto usuarioDtoAlterado = new UsuarioDto(1L, "Maria", "senhaalterada");

        when(usuarioService.alterarUsuario(1L, usuarioDto))
                .thenReturn(usuarioDtoAlterado);

        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarUsuario(1L, usuarioDto);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        UsuarioDto usuarioAlterado = resposta.getBody();
        assertNotNull(usuarioAlterado);
        assertEquals("senhaalterada", usuarioAlterado.getSenha());
    }

    @Test
    void naoDeveAlterarUsuarioInexistente() {
        when(usuarioService.alterarUsuario(anyLong(), any(UsuarioDto.class)))
                .thenThrow(new NotFoundException(ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<UsuarioDto> resposta = usuarioController.alterarUsuario(1L, new UsuarioDto(1L, "Maria", "senhaalterada"));
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarUsuarioExistente() {
        UsuarioDto usuario = new UsuarioDto(1L, "Maria");

        when(usuarioService.obterPorId(1L))
                .thenReturn(usuario);

        ResponseEntity<UsuarioDto> resposta = usuarioController.deletarUsuario(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(usuarioService).deletar(usuario.getId());
    }

    @Test
    void deveDarErroAoTentarDeletarUsuarioInexistente() {
        when(usuarioService.deletar(anyLong()))
                .thenThrow(new NotFoundException(ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<UsuarioDto> resposta = usuarioController.deletarUsuario(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}