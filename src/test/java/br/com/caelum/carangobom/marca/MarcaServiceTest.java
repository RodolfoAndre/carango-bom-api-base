package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    private MarcaService marcaService;

    private MarcaDtoMapper marcaDtoMapper;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        marcaDtoMapper = new MarcaDtoMapper();
        marcaService = new MarcaService(marcaRepository, marcaDtoMapper);
    }

    @Test
    void deveListarTodasAsMarcasCorretamente() {
        List<Marca> marcas = List.of(
                new Marca(1L, "Audi"),
                new Marca(2L, "BMW"),
                new Marca(3L, "Fiat")
        );

        when(marcaRepository.findAllByOrderByNome())
                .thenReturn(marcas);

        var marcasRetornadas = marcaService.listarMarcas();

        assertNotNull(marcasRetornadas);
        assertEquals(marcas, marcasRetornadas);
    }

    @Test
    void deveRetornarListaVaziaSeNaoEncontrarNenhumaMarca() {
        List<Marca> marcas = new ArrayList<Marca>();

        doReturn(marcas).when(marcaRepository).findAllByOrderByNome();

        var marcasRetornadas = marcaService.listarMarcas();

        assertNotNull(marcasRetornadas);
        assertEquals(marcas, marcasRetornadas);
    }

    @Test
    void deveRetornarMarcaPorIdCorretamente() {
        Optional<Marca> marcas = Optional.of(
                new Marca(1L, "Audi")
        );

        when(marcaRepository.findById(1L))
                .thenReturn(marcas);

        var marcasRetornadas = marcaService.obterMarcaPorId(1L);

        assertNotNull(marcasRetornadas);
        assertEquals(marcas.get(), marcasRetornadas);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoObterMarcaPorIdNaoEncontrarMarca() {
        Optional<Marca> marcas = Optional.empty();

        when(marcaRepository.findById(2L))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            marcaService.obterMarcaPorId(2L);
        });

        String expectedMessage = "Marca não encontrada";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarExcecaoCasoMarcaJaEstiverCadastradaAoTentarInserir() {
        MarcaDto marca = new MarcaDto(null, "Audi");

        Optional<Marca> marcaCadastrada = Optional.of(
                new Marca(1L, "Audi")
        );

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(marcaCadastrada);

        Exception exception = assertThrows(ConflictException.class, () -> {
            marcaService.cadastrarMarca(marca);
        });

        String expectedMessage = "Marca " + marcaCadastrada.get().getNome()+ " já existente";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarNovaMarcaCadastrada() {
        MarcaDto marca = new MarcaDto(null, "Audi");
        Marca novaMarca = new Marca(1L, "Audi");

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(Optional.empty());

        when(marcaRepository.save(any(Marca.class)))
                .thenReturn(novaMarca);

        var marcaCadastrada = marcaService.cadastrarMarca(marca);

        assertEquals(marca.getNome(), marcaCadastrada.getNome());
    }

    @Test
    void deveRetornarExcecaoCasoExistaMarcaComMesmoNomeAoTentarEditar() {
        MarcaDto marca = new MarcaDto(1L, "Audi");

        Optional<Marca> marcaCadastrada = Optional.of(
                new Marca(3L, "Audi")
        );

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(marcaCadastrada);

        Exception exception = assertThrows(ConflictException.class, () -> {
            marcaService.alterarMarca(marca.getId(), marca);
        });

        String expectedMessage = "Marca " + marcaCadastrada.get().getNome()+ " já existente";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoMarcaPorIdNaoEncontrarMarcaAoEditar() {
        Optional<Marca> marcas = Optional.empty();
        MarcaDto marca = new MarcaDto(1L, "Ferrari");

        when(marcaRepository.findById(2L))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            marcaService.alterarMarca(2L, marca);
        });

        String expectedMessage = "Marca não encontrada";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarMarcaAlteradaAoAlterarMarca() {
        MarcaDto marca = new MarcaDto(1L, "Ferrari");
        Optional<Marca> marcas = Optional.of(
                new Marca(1L, "Audi")
        );

        when(marcaRepository.findById(1L))
                .thenReturn(marcas);

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(Optional.empty());

        var marcaAlterada = marcaService.alterarMarca(1L, marca);

        assertEquals(marcas.get().getNome(), marcaAlterada.getNome());
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoMarcaPorIdNaoEncontrarMarcaAoExcluir() {
        Optional<Marca> marcas = Optional.empty();

        when(marcaRepository.findById(2L))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            marcaService.deletarMarca(2L);
        });

        String expectedMessage = "Marca não encontrada";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarMarcaExcluidaAoExcluirMarca() {
        Optional<Marca> marcas = Optional.of(
                new Marca(1L, "Audi")
        );

        when(marcaRepository.findById(1L))
                .thenReturn(marcas);

        var marcaAlterada = marcaService.deletarMarca(1L);

        assertEquals(marcas.get().getNome(), marcaAlterada.getNome());
    }

}