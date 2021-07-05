package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaRepository;
import br.com.caelum.carangobom.shared.estrutura.GenericCRUDService;
import br.com.caelum.carangobom.shared.filtro.GenericSpecificationsBuilder;
import br.com.caelum.carangobom.shared.filtro.SpecificationFactory;
import br.com.caelum.carangobom.veiculo.dashboard.DashboardUtils;
import br.com.caelum.carangobom.veiculo.dashboard.SumarioMarcaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe responsável pela lógica de negócios de veículos
 */
@Service
public class VeiculoService extends GenericCRUDService<Veiculo, VeiculoDto> {

    private VeiculoRepository veiculoRepository;

    private MarcaRepository marcaRepository;

    private VeiculoDtoMapper veiculoDtoMapper;

    private SpecificationFactory<Veiculo> veiculoSpecificationFactory;

    /**
     * Construtor de veículo service
     *
     * @param veiculoRepository o repositório de veículos
     * @param marcaRepository o repositório de marcas
     * @param veiculoDtoMapper o conversor de objeto de transferência para entidade
     */
    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository, MarcaRepository marcaRepository, VeiculoDtoMapper veiculoDtoMapper, SpecificationFactory<Veiculo> veiculoSpecificationFactory) {
        super(veiculoRepository, veiculoDtoMapper);
        this.veiculoRepository = veiculoRepository;
        this.marcaRepository = marcaRepository;
        this.veiculoDtoMapper = veiculoDtoMapper;
        this.veiculoSpecificationFactory = veiculoSpecificationFactory;
    }

    /**
     * Cadastra um veículo
     *
     * @param veiculoDto o veículo a ser cadastrado
     * @return o veículo cadastrado no banco de dados
     */
    @Transactional
    public VeiculoDto cadastrarVeiculo(VeiculoDto veiculoDto){
        var marca = obterMarca(veiculoDto.getMarca());
        var novoVeiculo = veiculoDtoMapper.converterParaEntidade(veiculoDto);
        novoVeiculo.setMarca(marca);

        return salvar(novoVeiculo);
    }

    /**
     * Altera um veículo
     *
     * @param id o id do veículo a ser alterado
     * @param veiculoDto os novos valores do veículo
     * @return o veículo alterado no banco de dados
     */
    @Transactional
    public VeiculoDto alterarVeiculo(Long id, VeiculoDto veiculoDto) {
        var marca = obterMarca(veiculoDto.getMarca());
        var veiculoEncontrado = veiculoDtoMapper.converterParaEntidade(obterPorId(id));
        veiculoEncontrado.setMarca(marca);

        veiculoEncontrado.setModelo(veiculoDto.getModelo());
        veiculoEncontrado.setAno(veiculoDto.getAno());
        veiculoEncontrado.setValor(veiculoDto.getValor());
        veiculoEncontrado.setMarca(marca);

        return salvar(veiculoEncontrado);
    }

    /**
     * Obtem marca pelo nome da marca
     *
     * @param nomeMarca o nome da marca a ser verificada
     */
    private Marca obterMarca(String nomeMarca) {
        Optional<Marca> marcaEncontrada = marcaRepository.findByNome(nomeMarca);
        return marcaEncontrada.orElseThrow(() -> new NotFoundException("Marca informada não encontrada"));
    }

    /**
     * Filtra as marcas pelos criterios dados
     *
     * @param filtroDto os filtros a serem utilizados na recuperação de marcas
     */
    public List<VeiculoDto> filtrarVeiculos(VeiculoFiltroDto filtroDto) {
        GenericSpecificationsBuilder<Veiculo> genericSpecificationsBuilder = new GenericSpecificationsBuilder<>();

        if (!CollectionUtils.isEmpty(filtroDto.getMarcas())) {
            List<Marca> marcas = marcaRepository.findAllByNomeIn(filtroDto.getMarcas());
            genericSpecificationsBuilder.with(veiculoSpecificationFactory.in("marca", marcas));
        }

        if (!CollectionUtils.isEmpty(filtroDto.getModelos())) {
            genericSpecificationsBuilder.with(veiculoSpecificationFactory.in("modelo", filtroDto.getModelos()));
        }

        if (Objects.nonNull(filtroDto.getPrecoMinimo())) {
            genericSpecificationsBuilder.with(veiculoSpecificationFactory.isGreaterThan("valor", filtroDto.getPrecoMinimo()));
        }

        if (Objects.nonNull(filtroDto.getPrecoMaximo())) {
            genericSpecificationsBuilder.with(veiculoSpecificationFactory.isLessThan("valor", filtroDto.getPrecoMaximo()));
        }

        List<Veiculo> veiculos = veiculoRepository.findAll(genericSpecificationsBuilder.build(), Sort.by(Sort.Direction.ASC, "id"));
        return veiculos.stream().map(veiculoDtoMapper::converterParaDto).collect(Collectors.toList());
    }

    /**
     * Cria o sumário de marca/modelos para ser utilizado no dashboard
     *
     * @return uma lista de sumário de marcas
     */
    public Set<SumarioMarcaDto> dashboard() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return DashboardUtils.criarSumarios(veiculos);
    }
}
