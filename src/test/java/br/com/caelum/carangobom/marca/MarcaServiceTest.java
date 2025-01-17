package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.exception.MensagensExcecoes;
import br.com.caelum.carangobom.exception.NotFoundException;
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

class MarcaServiceTest {

    public static final String MARCA_FERRARI = "Ferrari";
    public static final String MARCA_AUDI = "Audi";

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
                new Marca(1L, MARCA_AUDI),
                new Marca(2L, "BMW"),
                new Marca(3L, "Fiat")
        );
        List<MarcaDto> marcasEsperadas = marcas.stream().map(marcaDtoMapper::converterParaDto).collect(Collectors.toList());

        when(marcaRepository.findAll())
                .thenReturn(marcas);

        var marcasRetornadas = marcaService.listar();

        assertNotNull(marcasRetornadas);
        assertEquals(marcasEsperadas, marcasRetornadas);
    }

    @Test
    void deveRetornarListaVaziaSeNaoEncontrarNenhumaMarca() {
        List<MarcaDto> marcas = new ArrayList<>();

        doReturn(marcas).when(marcaRepository).findAllByOrderByNome();

        var marcasRetornadas = marcaService.listar();

        assertNotNull(marcasRetornadas);
        assertEquals(marcas, marcasRetornadas);
    }

    @Test
    void deveRetornarMarcaPorIdCorretamente() {
        Optional<Marca> marca = Optional.of(
                new Marca(1L, MARCA_AUDI)
        );
        var marcaEsperada = marcaDtoMapper.converterParaDto(marca.get());

        when(marcaRepository.findById(1L))
                .thenReturn(marca);

        var marcasRetornadas = marcaService.obterPorId(1L);

        assertNotNull(marcasRetornadas);
        assertEquals(marcaEsperada, marcasRetornadas);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoObterMarcaPorIdNaoEncontrarMarca() {
        Optional<Marca> marcas = Optional.empty();

        when(marcaRepository.findById(2L))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            marcaService.obterPorId(2L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarExcecaoCasoMarcaJaEstiverCadastradaAoTentarInserir() {
        MarcaDto marca = new MarcaDto(null, MARCA_AUDI);

        Optional<Marca> marcaCadastrada = Optional.of(
                new Marca(1L, MARCA_AUDI)
        );

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(marcaCadastrada);

        Exception exception = assertThrows(ConflictException.class, () -> {
            marcaService.cadastrarMarca(marca);
        });

        String expectedMessage = String.format(MensagensExcecoes.ENTIDADE_EXISTENTE_FORMATO_MENSAGEM, Marca.class.getSimpleName(), marcaCadastrada.get().getNome());
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarNovaMarcaCadastrada() {
        MarcaDto marca = new MarcaDto(null, MARCA_AUDI);
        Marca novaMarca = new Marca(1L, MARCA_AUDI);

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(Optional.empty());

        when(marcaRepository.save(any(Marca.class)))
                .thenReturn(novaMarca);

        var marcaCadastrada = marcaService.cadastrarMarca(marca);

        assertEquals(marca.getNome(), marcaCadastrada.getNome());
    }

    @Test
    void deveRetornarExcecaoCasoExistaMarcaComMesmoNomeAoTentarEditar() {
        MarcaDto marca = new MarcaDto(1L, MARCA_AUDI);

        Optional<Marca> marcaCadastrada = Optional.of(
                new Marca(3L, MARCA_AUDI)
        );

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(marcaCadastrada);

        Long id = marca.getId();

        Exception exception = assertThrows(ConflictException.class, () -> {
            marcaService.alterarMarca(id, marca);
        });

        String expectedMessage = String.format(MensagensExcecoes.ENTIDADE_EXISTENTE_FORMATO_MENSAGEM, Marca.class.getSimpleName(), marcaCadastrada.get().getNome());
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoMarcaPorIdNaoEncontrarMarcaAoEditar() {
        Optional<Marca> marcas = Optional.empty();
        MarcaDto marca = new MarcaDto(1L, MARCA_FERRARI);

        when(marcaRepository.findById(2L))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            marcaService.alterarMarca(2L, marca);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarMarcaAlteradaAoAlterarMarca() {
        MarcaDto marcaDto = new MarcaDto(1L, MARCA_FERRARI);
        Optional<Marca> marcaRegistrada = Optional.of(
                new Marca(1L, MARCA_AUDI)
        );
        Marca marca = new Marca(1L, MARCA_FERRARI);

        when(marcaRepository.findById(1L))
                .thenReturn(marcaRegistrada);

        when(marcaRepository.findByNome(marca.getNome()))
                .thenReturn(Optional.empty());

        when(marcaRepository.save(any(Marca.class)))
                .thenReturn(marca);

        var marcaAlterada = marcaService.alterarMarca(1L, marcaDto);

        assertEquals(MARCA_FERRARI, marcaAlterada.getNome());
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoMarcaPorIdNaoEncontrarMarcaAoExcluir() {
        Optional<Marca> marcas = Optional.empty();

        when(marcaRepository.findById(2L))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            marcaService.deletar(2L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarMarcaExcluidaAoExcluirMarca() {
        Optional<Marca> marcas = Optional.of(
                new Marca(1L, MARCA_AUDI)
        );

        when(marcaRepository.findById(1L))
                .thenReturn(marcas);

        var marcaDeletada = marcaService.deletar(1L);

        assertEquals(marcas.get().getNome(), marcaDeletada.getNome());
    }

}