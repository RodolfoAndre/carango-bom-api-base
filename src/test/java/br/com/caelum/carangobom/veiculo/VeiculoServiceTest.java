package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDtoMapper;
import br.com.caelum.carangobom.marca.MarcaRepository;
import br.com.caelum.carangobom.marca.MarcaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    private VeiculoService veiculoService;

    private VeiculoDtoMapper veiculoDtoMapper;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        veiculoDtoMapper = new VeiculoDtoMapper();
        veiculoService = new VeiculoService(veiculoRepository, veiculoDtoMapper);
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

        when(veiculoRepository.findAllByOrderByModelo())
                .thenReturn(veiculos);

        var veiculosRetornados = veiculoService.listarVeiculos();

        assertNotNull(veiculosRetornados);
        assertEquals(veiculos, veiculosRetornados);
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

        Optional<Veiculo> veiculos = Optional.of(
                new Veiculo(1L, "KA", 2008, 15.000, marcas.get(0))
        );

        when(veiculoRepository.findById(1L))
                .thenReturn(veiculos);

        var veiculosRetornados = veiculoService.obterVeiculoPorId(1L);

        assertNotNull(veiculosRetornados);
        assertEquals(veiculos.get(), veiculosRetornados);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoObterVeiculoPorIdNaoEncontrarVeiculo() {
        Optional<Veiculo> veiculos = Optional.empty();

        when(veiculoRepository.findById(2L))
                .thenReturn(veiculos);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.obterVeiculoPorId(2L);
        });

        String expectedMessage = "Veículo não encontrado";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveRetornarExcecaoNotFoundCasoVeiculoPorIdNaoEncontrarVeiculoAoExcluir() {
        Optional<Veiculo> veiculos = Optional.empty();

        when(veiculoRepository.findById(2L))
                .thenReturn(veiculos);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.deletarVeiculo(2L);
        });

        String expectedMessage = "Veículo não encontrado";
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