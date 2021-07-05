package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.MarcaDto;
import br.com.caelum.carangobom.veiculo.dashboard.SumarioMarcaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class VeiculoControllerTest {

    public static final String ENTIDADE_NAO_ENCONTRADO_MENSAGEM = "Entidade não encontrada";

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

        when(veiculoService.listar())
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

        when(veiculoService.obterPorId(1L))
                .thenReturn(veiculo);

        ResponseEntity<VeiculoDto> resposta = veiculoController.obterVeiculoPorId(1L);
        assertEquals(veiculo, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoTentarBuscarVeiculoComIdInexistente() {
        when(veiculoService.obterPorId(anyLong()))
                .thenThrow(new NotFoundException(ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<VeiculoDto> resposta = veiculoController.obterVeiculoPorId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarVeiculo() {
        MarcaDto marcaDto = new MarcaDto(1L, "Ford");
        VeiculoDto novoVeiculoDto = new VeiculoDto(null, "KA", 2008, 15.000, marcaDto.getNome());

        when(veiculoService.cadastrarVeiculo(novoVeiculoDto))
                .thenReturn(new VeiculoDto(1L, "KA", 2008, 15.000, marcaDto.getNome()));

        ResponseEntity<VeiculoDto> resposta = veiculoController.cadastrarVeiculo(novoVeiculoDto, uriBuilder);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("http://localhost:8080/veiculos/1", resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveAlterarValorVeiculo() {
        MarcaDto marcaDto = new MarcaDto(1L, "Ford");
        VeiculoDto veiculoDto = new VeiculoDto(1L, "KA", 2008, 15.000, marcaDto.getNome());
        VeiculoDto veiculoDtoAlterado = new VeiculoDto(1L, "KA", 2008, 16.700, marcaDto.getNome());

        when(veiculoService.alterarVeiculo(1L, veiculoDto))
                .thenReturn(veiculoDtoAlterado);

        ResponseEntity<VeiculoDto> resposta = veiculoController.alterarVeiculo(1L, veiculoDto);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        VeiculoDto veiculoAlterado = resposta.getBody();
        assertNotNull(veiculoAlterado);
        assertEquals(16.700, veiculoAlterado.getValor());
    }

    @Test
    void naoDeveAlterarVeiculoInexistente() {
        MarcaDto marcaDto = new MarcaDto(1L, "Ford");
        when(veiculoService.alterarVeiculo(anyLong(), any(VeiculoDto.class)))
                .thenThrow(new NotFoundException(ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<VeiculoDto> resposta = veiculoController.alterarVeiculo(1L, new VeiculoDto(1L, "KA", 2008, 16.700, marcaDto.getNome()));
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarVeiculoExistente() {
        List<MarcaDto> marcas = List.of(
                new MarcaDto(1L, "Ford")
        );

        VeiculoDto veiculo = new VeiculoDto(1L, "KA", 2008, 15.000, marcas.get(0).getNome());

        when(veiculoService.obterPorId(1L))
                .thenReturn(veiculo);

        ResponseEntity<VeiculoDto> resposta = veiculoController.deletarVeiculo(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(veiculoService).deletar(veiculo.getId());
    }

    @Test
    void deveDarErroAoTentarDeletarVeiculoInexistente() {
        when(veiculoService.deletar(anyLong()))
                .thenThrow(new NotFoundException(ENTIDADE_NAO_ENCONTRADO_MENSAGEM));

        ResponseEntity<VeiculoDto> resposta = veiculoController.deletarVeiculo(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveRetornarObjetosFiltrados() {
        VeiculoFiltroDto veiculoFiltroDto = new VeiculoFiltroDto();
        when(veiculoService.filtrarVeiculos(veiculoFiltroDto)).thenReturn(new ArrayList<>());

        ResponseEntity<List<VeiculoDto>> resposta = veiculoController.filtrarVeiculos(veiculoFiltroDto);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarDashboard() {
        when(veiculoService.dashboard()).thenReturn(new HashSet<>());
        ResponseEntity<Set<SumarioMarcaDto>> resposta = veiculoController.dashboard();
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}