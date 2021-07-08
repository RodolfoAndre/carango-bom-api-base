package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.MensagensExcecoes;
import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.*;
import br.com.caelum.carangobom.shared.filtro.SpecificationFactory;
import br.com.caelum.carangobom.veiculo.dashboard.SumarioMarcaDto;
import br.com.caelum.carangobom.veiculo.dashboard.SumarioModeloDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class VeiculoServiceTest {

    public static final String FORD_MARCA_CONSTANTE = "Ford";
    public static final String GM_MARCA_CONSTANTE = "GM";
    public static final String KA_MODELO_CONSTANTE = "Ka";
    public static final String CORSA_MODELO_CONSTANTE = "Corsa";
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
        SpecificationFactory<Veiculo> veiculoSpecificationFactory = new SpecificationFactory<Veiculo>();
        veiculoService = new VeiculoService(veiculoRepository, marcaRepository, veiculoDtoMapper, veiculoSpecificationFactory);
    }

    @Test
    void deveListarTodasOsVeiculosCorretamente() {
        List<Marca> marcas = List.of(
                new Marca(1L, FORD_MARCA_CONSTANTE),
                new Marca(2L, GM_MARCA_CONSTANTE)
        );

        List<Veiculo> veiculos = List.of(
                new Veiculo(1L, KA_MODELO_CONSTANTE, 2008, 15.000, marcas.get(0)),
                new Veiculo(2L, CORSA_MODELO_CONSTANTE, 2008, 15.000, marcas.get(1))
        );

        List<VeiculoDto> veiculosEsperados = veiculos.stream().map(veiculoDtoMapper::converterParaDto).collect(Collectors.toList());

        when(veiculoRepository.findAll())
                .thenReturn(veiculos);

        var veiculosRetornados = veiculoService.listar();

        assertNotNull(veiculosRetornados);
        assertEquals(veiculosEsperados, veiculosRetornados);
    }

    @Test
    void deveRetornarListaVaziaSeNaoEncontrarNenhumVeiculo() {
        List<VeiculoDto> veiculos = new ArrayList<>();

        doReturn(veiculos).when(veiculoRepository).findAllByOrderByModelo();

        var veiculosRetornados = veiculoService.listar();

        assertNotNull(veiculosRetornados);
        assertEquals(veiculos, veiculosRetornados);
    }

    @Test
    void deveRetornarVeiculoPorIdCorretamente() {
        List<Marca> marcas = List.of(
                new Marca(1L, FORD_MARCA_CONSTANTE)
        );

        Optional<Veiculo> veiculo = Optional.of(
                new Veiculo(1L, KA_MODELO_CONSTANTE, 2008, 15.000, marcas.get(0))
        );

        var veiculoEsperado = veiculoDtoMapper.converterParaDto(veiculo.get());

        when(veiculoRepository.findById(1L))
                .thenReturn(veiculo);

        var veiculosRetornados = veiculoService.obterPorId(1L);

        assertNotNull(veiculosRetornados);
        assertEquals(veiculoEsperado, veiculosRetornados);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoObterVeiculoPorIdNaoEncontrarVeiculo() {
        Optional<Veiculo> veiculos = Optional.empty();

        when(veiculoRepository.findById(2L))
                .thenReturn(veiculos);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.obterPorId(2L);
        });
        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarExcecaoNotFoundAoTentarCadastrarVeiculoEObterMarcaPorNomeNaoEncontrarMarca() {
        Optional<Marca> marcas = Optional.empty();
        VeiculoDto veiculoDto = new VeiculoDto(null, KA_MODELO_CONSTANTE, 2008, 15.000, FORD_MARCA_CONSTANTE);

        when(marcaRepository.findByNome(veiculoDto.getMarca()))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.cadastrarVeiculo(veiculoDto);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.MARCA_NAO_ECONTRADA, actualMessage);
    }

    @Test
    void deveRetornarNovoVeiculoCadastrado() {
        Optional<Marca> marca = Optional.of(
                new Marca(1L, FORD_MARCA_CONSTANTE)
        );
        VeiculoDto veiculoDto = new VeiculoDto(null, KA_MODELO_CONSTANTE, 2008, 15.000, FORD_MARCA_CONSTANTE);
        Veiculo novoVeiculo = new Veiculo(1L, KA_MODELO_CONSTANTE, 2008, 15.000, marca.get());

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
        VeiculoDto veiculoDto = new VeiculoDto(1L, KA_MODELO_CONSTANTE, 2008, 15.000, FORD_MARCA_CONSTANTE);

        when(marcaRepository.findByNome(veiculoDto.getMarca()))
                .thenReturn(marcas);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            veiculoService.alterarVeiculo(1L, veiculoDto);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.MARCA_NAO_ECONTRADA, actualMessage);
    }

    @Test
    void deveRetornarValorVeiculoAlteradoAoAlterarVeiculo() {
        Optional<Marca> marca = Optional.of(
                new Marca(1L, FORD_MARCA_CONSTANTE)
        );
        Optional<Veiculo> veiculo = Optional.of(
                new Veiculo(1L, KA_MODELO_CONSTANTE, 2008, 15.000, marca.get())
        );
        VeiculoDto veiculoDto = new VeiculoDto(1L, KA_MODELO_CONSTANTE, 2008, 17.800, FORD_MARCA_CONSTANTE);
        Veiculo veiculoAlterado = new Veiculo(1L, KA_MODELO_CONSTANTE, 2008, 17.800, marca.get());

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
            veiculoService.deletar(2L);
        });

        String actualMessage = exception.getMessage();

        assertEquals(MensagensExcecoes.ENTIDADE_NAO_ENCONTRADO_MENSAGEM, actualMessage);
    }

    @Test
    void deveRetornarVeiculoExcluidoAoExcluirVeiculo() {
        List<Marca> marcas = List.of(
                new Marca(1L, FORD_MARCA_CONSTANTE)
        );

        Optional<Veiculo> veiculos = Optional.of(
                new Veiculo(1L, KA_MODELO_CONSTANTE, 2008, 15.000, marcas.get(0))
        );

        when(veiculoRepository.findById(1L))
                .thenReturn(veiculos);

        var veiculoDeletado = veiculoService.deletar(1L);

        assertEquals(veiculos.get().getModelo(), veiculoDeletado.getModelo());
    }

    @Test
    void deveRetornarVeiculosAoFiltrarVeiculos() {
        VeiculoFiltroDto filtroDto = new VeiculoFiltroDto();
        filtroDto.setMarcas(Set.of("Ferrai", FORD_MARCA_CONSTANTE));
        filtroDto.setModelos(Set.of("488", KA_MODELO_CONSTANTE));
        filtroDto.setPrecoMaximo(10000D);
        filtroDto.setPrecoMinimo(15000D);

        Veiculo veiculo = new Veiculo(1L, KA_MODELO_CONSTANTE, 2020, 18000D);
        veiculo.setMarca(new Marca(FORD_MARCA_CONSTANTE));
        List<Veiculo> veiculos = Collections.singletonList(veiculo);
        when(veiculoRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(veiculos);

        var veiculosFiltrados = veiculoService.filtrarVeiculos(filtroDto);

        assertEquals(veiculos.size(), veiculosFiltrados.size());
    }

    @Test
    void deveRetornarSumarioNaRecuperacaoDeDashboard() {
        VeiculoFiltroDto filtroDto = new VeiculoFiltroDto();
        filtroDto.setMarcas(Set.of("Ferrai", FORD_MARCA_CONSTANTE));
        filtroDto.setModelos(Set.of("488", KA_MODELO_CONSTANTE));
        filtroDto.setPrecoMaximo(10000D);
        filtroDto.setPrecoMinimo(15000D);

        Veiculo veiculo0 = new Veiculo(1L, KA_MODELO_CONSTANTE, 2019, 18000D);
        veiculo0.setMarca(new Marca(FORD_MARCA_CONSTANTE));

        Veiculo veiculo1 = new Veiculo(1L, KA_MODELO_CONSTANTE, 2020, 22000D);
        veiculo1.setMarca(new Marca(FORD_MARCA_CONSTANTE));

        Veiculo veiculo2 = new Veiculo(1L, "Fiesta", 2020, 50000D);
        veiculo2.setMarca(new Marca(FORD_MARCA_CONSTANTE));

        List<Veiculo> veiculos = Arrays.asList(veiculo0, veiculo1, veiculo2);
        when(veiculoRepository.findAll()).thenReturn(veiculos);

        var dashboard = veiculoService.dashboard();

        Assertions.assertNotNull(dashboard);
        Assertions.assertFalse(CollectionUtils.isEmpty(dashboard));
        for (SumarioMarcaDto sumarioMarcaDto : dashboard) {
            Assertions.assertEquals(FORD_MARCA_CONSTANTE, sumarioMarcaDto.getMarca());
            Assertions.assertEquals(90000, sumarioMarcaDto.getValorTotal());
            Assertions.assertEquals(3, sumarioMarcaDto.getNumeroDeVeiculos());

            for (SumarioModeloDto modelo : sumarioMarcaDto.getModelos()) {
                if (modelo.getModelo().equals(KA_MODELO_CONSTANTE)){
                    Assertions.assertEquals(2, modelo.getNumeroDeVeiculos());
                    Assertions.assertEquals(40000, modelo.getValorTotal());
                } else if (modelo.getModelo().equals("Fiesta")) {
                    Assertions.assertEquals(1, modelo.getNumeroDeVeiculos());
                    Assertions.assertEquals(50000, modelo.getValorTotal());
                } else {
                    Assertions.fail();
                }
            }
        }
    }
}