package br.com.caelum.carangobom.veiculo;

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

class VeiculoServiceTest {

    public static final String VEICULO_NAO_ENCONTRADO_MENSAGEM = "Veículo não encontrado";
    public static final String MARCA_NAO_ENCONTRADA_MENSAGEM = "Marca informada não encontrada";

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    private VeiculoService veiculoService;

    private VeiculoDtoMapper veiculoDtoMapper;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        veiculoDtoMapper = new VeiculoDtoMapper();
        veiculoService = new VeiculoService(veiculoRepository, marcaRepository, veiculoDtoMapper);
    }

    @Test
    void deveListarTodasOsVeiculosCorretamente() {
        List<Marca> marcas = List.of(
                new Marca(1L, "Ford"),
                new Marca(2L, "GM")
        );

        List<Veiculo> veiculos = List.of(
                new Veiculo(1L, "KA", 2008, 15.000, marcas.get(0)),
                new Veiculo(2L, "Corsa", 2008, 15.000, marcas.get(1))
        );

        List<VeiculoDto> veiculosEsperados = veiculos.stream().map(veiculoDtoMapper::map).collect(Collectors.toList());

        when(veiculoRepository.findAllByOrderByModelo())
                .thenReturn(veiculos);

        var veiculosRetornados = veiculoService.listarVeiculos();

        assertNotNull(veiculosRetornados);
        assertEquals(veiculosEsperados, veiculosRetornados);
    }

    @Test
    void deveRetornarListaVaziaSeNaoEncontrarNenhumVeiculo() {
        List<Veiculo> veiculos = new ArrayList<Veiculo>();

        doReturn(veiculos).when(veiculoRepository).findAllByOrderByModelo();

        var veiculosRetornados = veiculoService.listarVeiculos();

        assertNotNull(veiculosRetornados);
        assertEquals(veiculos, veiculosRetornados);
    }

    @Test
    void deveRetornarVeiculoPorIdCorretamente() {
        List<Marca> marcas = List.of(
                new Marca(1L, "Ford")
        );

        Optional<Veiculo> veiculo = Optional.of(
                new Veiculo(1L, "KA", 2008, 15.000, marcas.get(0))
        );

        var veiculoEsperado = veiculoDtoMapper.map(veiculo.get());

        when(veiculoRepository.findById(1L))
                .thenReturn(veiculo);

        var veiculosRetornados = veiculoService.obterVeiculoPorId(1L);

        assertNotNull(veiculosRetornados);
        assertEquals(veiculoEsperado, veiculosRetornados);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoObterVeiculoPorIdNaoEncontrarVeiculo() {
        Optional<Veiculo> veiculos = Optional.empty();

        when(veiculoRepository.findById(2L))
                .thenReturn(veiculos);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.obterVeiculoPorId(2L);
        });

        String expectedMessage = VEICULO_NAO_ENCONTRADO_MENSAGEM;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoTentarCadastrarVeiculoEObterMarcaPorNomeNaoEncontrarMarca() {
        Optional<Marca> marcas = Optional.empty();
        VeiculoDto veiculoDto = new VeiculoDto(null, "KA", 2008, 15.000, "Ford");

        when(marcaRepository.findByNome(veiculoDto.getMarca()))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.cadastrarVeiculo(veiculoDto);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MARCA_NAO_ENCONTRADA_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarNovoVeiculoCadastrado() {
        Optional<Marca> marca = Optional.of(
                new Marca(1L, "Ford")
        );
        VeiculoDto veiculoDto = new VeiculoDto(null, "KA", 2008, 15.000, "Ford");
        Veiculo novoVeiculo = new Veiculo(1L, "KA", 2008, 15.000, marca.get());

        when(marcaRepository.findByNome(veiculoDto.getMarca()))
                .thenReturn(marca);

        when(veiculoRepository.save(any(Veiculo.class)))
                .thenReturn(novoVeiculo);

        var veiculoCadastrado = veiculoService.cadastrarVeiculo(veiculoDto);

        assertEquals(veiculoDto.getModelo(), veiculoCadastrado.getModelo());
    }

    @Test
    void deveRetornarExcecaoNotFoundAoTentarEditarVeiculoEObterMarcaPorNomeNaoEncontrarMarca() {
        Optional<Marca> marcas = Optional.empty();
        VeiculoDto veiculoDto = new VeiculoDto(1L, "KA", 2008, 15.000, "Ford");

        when(marcaRepository.findByNome(veiculoDto.getMarca()))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.alterarVeiculo(1L, veiculoDto);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MARCA_NAO_ENCONTRADA_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarValorVeiculoAlteradoAoAlterarVeiculo() {
        Optional<Marca> marca = Optional.of(
                new Marca(1L, "Ford")
        );
        Optional<Veiculo> veiculo = Optional.of(
                new Veiculo(1L, "KA", 2008, 15.000, marca.get())
        );
        VeiculoDto veiculoDto = new VeiculoDto(1L, "KA", 2008, 17.800, "Ford");
        Veiculo veiculoAlterado = new Veiculo(1L, "KA", 2008, 17.800, marca.get());

        when(marcaRepository.findByNome(veiculoDto.getMarca()))
                .thenReturn(marca);

        when(veiculoRepository.findById(1L))
                .thenReturn(veiculo);

        when(veiculoRepository.save(any(Veiculo.class)))
                .thenReturn(veiculoAlterado);

        var veiculoAlteradoDto = veiculoService.alterarVeiculo(1L, veiculoDto);

        assertEquals(17.800, veiculoAlteradoDto.getValor());
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoVeiculoPorIdNaoEncontrarVeiculoAoExcluir() {
        Optional<Veiculo> veiculos = Optional.empty();

        when(veiculoRepository.findById(2L))
                .thenReturn(veiculos);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.deletarVeiculo(2L);
        });

        String expectedMessage = VEICULO_NAO_ENCONTRADO_MENSAGEM;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarVeiculoExcluidoAoExcluirVeiculo() {
        List<Marca> marcas = List.of(
                new Marca(1L, "Ford")
        );

        Optional<Veiculo> veiculos = Optional.of(
                new Veiculo(1L, "KA", 2008, 15.000, marcas.get(0))
        );

        when(veiculoRepository.findById(1L))
                .thenReturn(veiculos);

        var veiculoDeletado = veiculoService.deletarVeiculo(1L);

        assertEquals(veiculos.get().getModelo(), veiculoDeletado.getModelo());
    }
}