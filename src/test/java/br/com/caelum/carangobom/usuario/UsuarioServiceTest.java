package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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

    public static final String ENTIDADE_NAO_ENCONTRADO_MENSAGEM = "Entidade não encontrada";

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

//    @Test
//    void deveListarTodasOsUsuariosCorretamente() {
//
//        List<UsuarioDto> usuariosEsperados = usuarios.stream().map(usuarioDtoMapper::converterParaDto).collect(Collectors.toList());
//
//        when(usuarioRepository.findAll())
//                .thenReturn(usuarios);
//
//        var usuariosRetornados = usuarioService.listar();
//
//        assertNotNull(usuariosRetornados);
//        assertEquals(usuariosEsperados, usuariosRetornados);
//    }

    @Test
    void deveRetornarListaVaziaSeNaoEncontrarNenhumUsuario() {
        List<UsuarioDto> usuarios = new ArrayList<>();

       // doReturn(usuarios).when(usuarioService).listar();

        var usariosRetornados = usuarioService.listar();

        assertNotNull(usariosRetornados);
        assertEquals(usuarios, usariosRetornados);
    }
}