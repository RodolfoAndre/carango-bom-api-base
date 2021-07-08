package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.exception.MensagensExcecoes;
import br.com.caelum.carangobom.exception.NotAllowedException;
import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioServiceTest {

    public static final String MARIA_STRING_CONSTANT = "Maria";
    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    private UsuarioDtoMapper usuarioDtoMapper;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        usuarioDtoMapper = new UsuarioDtoMapper();
        usuarioService = new UsuarioService(usuarioRepository, usuarioDtoMapper);
    }

    @Test
    void deveListarTodasOsUsuariosCorretamente() {
        List<Usuario> usuarios = new ArrayList<>();

        Usuario usuarioMaria = new Usuario();
        usuarioMaria.setId(1L);
        usuarioMaria.setNome(MARIA_STRING_CONSTANT);
        usuarioMaria.setSenha("senhamaria");

        Usuario usuarioJoao = new Usuario();
        usuarioJoao.setId(2L);
        usuarioJoao.setNome("João");
        usuarioJoao.setSenha("senhajoao");

        usuarios.add(usuarioMaria);
        usuarios.add(usuarioJoao);

        List<UsuarioDto> usuariosEsperados = usuarios.stream().map(usuarioDtoMapper::converterParaDto).collect(Collectors.toList());

        when(usuarioRepository.findAll())
                .thenReturn(usuarios);

        var usuariosRetornados = usuarioService.listar();

        assertNotNull(usuariosRetornados);
        assertEquals(usuariosEsperados.get(0).getNome(), usuariosRetornados.get(0).getNome());
        assertEquals(usuariosEsperados.get(1).getNome(), usuariosRetornados.get(1).getNome());
    }

    @Test
    void deveRetornarListaVaziaSeNaoEncontrarNenhumUsuario() {
        List<UsuarioDto> usuarios = new ArrayList<>();

        var usariosRetornados = usuarioService.listar();

        assertNotNull(usariosRetornados);
        assertEquals(usuarios, usariosRetornados);
    }

    @Test
    void deveRetornarUsuarioPorIdCorretamente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(MARIA_STRING_CONSTANT);
        usuario.setSenha("senhausuario");

        Optional<Usuario> usuarioMaria = Optional.of(usuario);

        var usuarioEsperado = usuarioDtoMapper.converterParaDto(usuarioMaria.get());

        when(usuarioRepository.findById(1L))
                .thenReturn(usuarioMaria);

        var usuarioRetornado = usuarioService.obterPorId(1L);

        assertNotNull(usuarioRetornado);
        assertEquals(usuarioEsperado.getNome(), usuarioRetornado.getNome());
    }

    @Test
    void deveRetornarExcecaoNotFoundAoObterUsuarioPorIdNaoEncontrarUsuario() {
        Optional<Usuario> usuario = Optional.empty();

        when(usuarioRepository.findById(2L))
                .thenReturn(usuario);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            usuarioService.obterPorId(2L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarExcecaoCasoUsuarioJaEstiverCadastradaAoTentarInserir() {
        UsuarioDto usuarioDto = new UsuarioDto(null, MARIA_STRING_CONSTANT, "senha123");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(MARIA_STRING_CONSTANT);
        usuario.setSenha("senhausuario");

        Optional<Usuario> usuarioCadastrado = Optional.of(usuario);

        when(usuarioRepository.findByNome(usuarioDto.getNome()))
                .thenReturn(usuarioCadastrado);

        Exception exception = assertThrows(ConflictException.class, () -> {
            usuarioService.cadastrarUsuario(usuarioDto);
        });

        String expectedMessage = "Usuario " + usuarioCadastrado.get().getNome()+ " já existente";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarNovoUsuarioCadastrado() {
        UsuarioDto usuarioDto = new UsuarioDto(null, MARIA_STRING_CONSTANT, "senhausuario");
        Usuario novoUsuario = new Usuario();
        novoUsuario.setId(1L);
        novoUsuario.setNome(MARIA_STRING_CONSTANT);
        novoUsuario.setSenha("senhausuario");

        when(usuarioRepository.findByNome(usuarioDto.getNome()))
                .thenReturn(Optional.empty());

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(novoUsuario);

        var usuarioCadastrado = usuarioService.cadastrarUsuario(usuarioDto);

        assertEquals(usuarioDto.getNome(), usuarioCadastrado.getNome());
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoUsuarioPorIdNaoEncontrarUsuarioAoEditar() {
        Optional<Usuario> usuario = Optional.empty();
        UsuarioDto usuarioDto = new UsuarioDto(1L, MARIA_STRING_CONSTANT);

        when(usuarioRepository.findById(2L))
                .thenReturn(usuario);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            usuarioService.alterarUsuario(1L, usuarioDto, 1L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarExcecaoNotAllowrdCasoUsuarioPorIdNaoSejaOUsuarioLogado() {
        Optional<Usuario> usuario = Optional.empty();
        UsuarioDto usuarioDto = new UsuarioDto(1L, MARIA_STRING_CONSTANT);

        when(usuarioRepository.findById(2L))
                .thenReturn(usuario);

        Exception exception = assertThrows(NotAllowedException.class, () -> {
            usuarioService.alterarUsuario(1L, usuarioDto, 2L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.TROCAR_SENHA_DE_OUTRO_USUARIO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarUsuarioAlteradoAoAlterarSenhaUsuario() {
        UsuarioDto usuarioDto = new UsuarioDto(1L, MARIA_STRING_CONSTANT, "senha123alterada");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(MARIA_STRING_CONSTANT);
        usuario.setSenha("senha123");
        Optional<Usuario> usuarioRegistrado = Optional.of(usuario);

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome(MARIA_STRING_CONSTANT);
        usuarioSalvo.setSenha("senha123alterada");

        when(usuarioRepository.findById(1L))
                .thenReturn(usuarioRegistrado);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuarioSalvo);

        var usuarioAlterado = usuarioService.alterarUsuario(1L, usuarioDto, 1L);

        assertNull( usuarioAlterado.getSenha());
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(any());
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoUsuarioPorIdNaoEncontrarUsuarioAoExcluir() {
        Optional<Usuario> usuario = Optional.empty();

        when(usuarioRepository.findById(2L))
                .thenReturn(usuario);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            usuarioService.deletar(2L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarUsuarioExcluidoAoExcluirUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(MARIA_STRING_CONSTANT);
        Optional<Usuario> usuarioExcluido = Optional.of(usuario);

        when(usuarioRepository.findById(1L))
                .thenReturn(usuarioExcluido);

        var usuarioDeletado = usuarioService.deletar(1L);

        assertEquals(usuarioExcluido.get().getNome(), usuarioDeletado.getNome());
    }
}