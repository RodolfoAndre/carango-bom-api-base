package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class VeiculoControllerTest {

    private UriComponentsBuilder uriBuilder;

    @Mock
    private VeiculoService veiculoService;

    private VeiculoController veiculoController;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);

        veiculoController = new VeiculoController(veiculoService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaDeVeiculosQuandoHouverResultados() {
        List<MarcaDto> marcas = List.of(
                new MarcaDto(1L, "Ford"),
                new MarcaDto(2L, "GM")
        );

        List<VeiculoDto> veiculos = List.of(
                new VeiculoDto(1L, "KA", 2008, 15.000, marcas.get(0).getNome()),
                new VeiculoDto(2L, "Corsa", 2008, 15.000, marcas.get(1).getNome())
        );

        when(veiculoService.listarVeiculos())
                .thenReturn(veiculos);

        ResponseEntity<List<VeiculoDto>> resultado = veiculoController.listarVeiculos();

        assertEquals(veiculos, resultado.getBody());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
    }

    @Test
    void deveRetornarVeiculoPeloId() {
        List<MarcaDto> marcas = List.of(
                new MarcaDto(1L, "Ford")
        );

        VeiculoDto veiculo = new VeiculoDto(1L, "KA", 2008, 15.000, marcas.get(0).getNome());

        when(veiculoService.obterVeiculoPorId(1L))
                .thenReturn(veiculo);

        ResponseEntity<VeiculoDto> resposta = veiculoController.obterVeiculoPorId(1L);
        assertEquals(veiculo, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoTentarBuscarVeiculoComIdInexistente() {
        when(veiculoService.obterVeiculoPorId(anyLong()))
                .thenThrow(new NotFoundException("Veículo não encontrado"));

        ResponseEntity<VeiculoDto> resposta = veiculoController.obterVeiculoPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarVeiculoExistente() {
        List<MarcaDto> marcas = List.of(
                new MarcaDto(1L, "Ford")
        );

        VeiculoDto veiculo = new VeiculoDto(1L, "KA", 2008, 15.000, marcas.get(0).getNome());

        when(veiculoService.obterVeiculoPorId(1L))
                .thenReturn(veiculo);

        ResponseEntity<VeiculoDto> resposta = veiculoController.deletarVeiculo(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(veiculoService).deletarVeiculo(veiculo.getId());
    }

    @Test
    void deveDarErroAoTentarDeletarVeiculoInexistente() {
        when(veiculoService.deletarVeiculo(anyLong()))
                .thenThrow(new NotFoundException("Veículo não encontrado"));

        ResponseEntity<VeiculoDto> resposta = veiculoController.deletarVeiculo(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}