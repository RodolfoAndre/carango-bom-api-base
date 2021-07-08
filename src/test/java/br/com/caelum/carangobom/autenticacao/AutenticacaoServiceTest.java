package br.com.caelum.carangobom.autenticacao;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private AutenticacaoService autenticacaoService;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        autenticacaoService = new AutenticacaoService(usuarioRepository);
    }

    @Test
    void deveRetornarUsuarioPeloNomeCorretamente() {
        Usuario userDetails = new Usuario();
        userDetails.setNome("Maria");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNome("Maria");
        usuarioMock.setSenha("senha123");

        Optional<Usuario> usuario = Optional.of(usuarioMock);

        when(usuarioRepository.findByNome("Maria"))
                .thenReturn(usuario);

        var usuarioRetornado = autenticacaoService.loadUserByUsername("Maria");

        assertNotNull(usuarioRetornado);
        assertEquals(userDetails.getUsername(), usuarioRetornado.getUsername());
    }

    @Test
    void deveRetornarNotfoundSeNaoEncontrarUsuario() {
        Optional<Usuario> usuario = Optional.empty();

        when(usuarioRepository.findByNome("Maria"))
                .thenReturn(usuario);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            autenticacaoService.loadUserByUsername("Maria");
        });

        String actualMessage = exception.getMessage();
        assertEquals("Dados inválidos!", actualMessage);
    }
}