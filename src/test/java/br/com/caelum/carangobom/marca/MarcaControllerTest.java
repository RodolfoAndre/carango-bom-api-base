package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaControllerTest {

    private UriComponentsBuilder uriBuilder;

    @Mock
    private MarcaService marcaService;

    private MarcaController marcaController;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        marcaController = new MarcaController(marcaService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() {
        List<Marca> marcas = List.of(
            new Marca(1L, "Audi"),
            new Marca(2L, "BMW"),
            new Marca(3L, "Fiat")
        );

        when(marcaService.listarMarcas())
            .thenReturn(marcas);

        ResponseEntity<List<Marca>> resultado = marcaController.listarMarcas();

        assertEquals(marcas, resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test
    void deveRetornarMarcaPeloId() {
        Marca audi = new Marca(1L, "Audi");

        when(marcaService.obterMarcaPorId(1L))
            .thenReturn(audi);

        ResponseEntity<Marca> resposta = marcaController.obterMarcarPorId(1L);
        assertEquals(audi, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() {
        when(marcaService.obterMarcaPorId(anyLong()))
                .thenThrow(new NotFoundException("Marca não encontrada"));

        ResponseEntity<Marca> resposta = marcaController.obterMarcarPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarMarca() {
        MarcaDto novaMarcaDto = new MarcaDto(null, "Ferrari");

        when(marcaService.cadastrarMarca(novaMarcaDto))
            .thenReturn(new Marca(1L, "Ferrari"));

        ResponseEntity<Marca> resposta = marcaController.cadastrarMarca(novaMarcaDto, uriBuilder);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("http://localhost:8080/marcas/1", resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarNomeQuandoMarcaExistir() {
        MarcaDto novaAudiDto = new MarcaDto(1L, "NOVA Audi");
        Marca novaAudi = new Marca(1L, "NOVA Audi");

        when(marcaService.alterarMarca(1L, novaAudiDto))
            .thenReturn(novaAudi);

        ResponseEntity<Marca> resposta = marcaController.alterarMarca(1L, novaAudiDto);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        Marca marcaAlterada = resposta.getBody();
        assertNotNull(marcaAlterada);
        assertEquals("NOVA Audi", marcaAlterada.getNome());
    }

    @Test
    void naoDeveAlterarMarcaInexistente() {
        when(marcaService.alterarMarca(anyLong(), any(MarcaDto.class)))
                .thenThrow(new NotFoundException("Marca não encontrada"));

        ResponseEntity<Marca> resposta = marcaController.alterarMarca(1L, new MarcaDto(1L, "NOVA Audi"));
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarMarcaExistente() {
        Marca audi = new Marca(1l, "Audi");

        when(marcaService.obterMarcaPorId(1L))
            .thenReturn(audi);

        ResponseEntity<Marca> resposta = marcaController.deletarMarca(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(marcaService).deletarMarca(audi.getId());
    }

    @Test
    void naoDeveDeletarMarcaInexistente() {
        when(marcaService.deletarMarca(anyLong()))
                .thenThrow(new NotFoundException("Marca não encontrada"));

        ResponseEntity<Marca> resposta = marcaController.deletarMarca(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}