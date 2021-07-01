package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDto;
import br.com.caelum.carangobom.marca.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável pela lógica de negócios de veículos
 */
@Service
public class VeiculoService {

    private VeiculoRepository veiculoRepository;

    private MarcaRepository marcaRepository;

    private VeiculoDtoMapper veiculoDtoMapper;

    /**
     * Construtor de veículo service
     *
     * @param veiculoRepository o repositório de veículos
     * @param marcaRepository o repositório de marcas
     * @param veiculoDtoMapper o conversor de objeto de transferência para entidade
     */
    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository, MarcaRepository marcaRepository, VeiculoDtoMapper veiculoDtoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.marcaRepository = marcaRepository;
        this.veiculoDtoMapper = veiculoDtoMapper;
    }

    /**
     * Lista todos os veículos que estão presentes no banco de dados
     *
     * @return uma {@link List} de {@link VeiculoDto}
     */
    @Transactional
    public List<VeiculoDto> listarVeiculos() {
        return veiculoRepository.findAllByOrderByModelo().stream().map(veiculoDtoMapper::map).collect(Collectors.toList());
    }

    /**
     * Obtém veículos por id
     *
     * @param id o id do veículo a ser obtido
     * @return a {@link VeiculoDto} encontrado
     */
    @Transactional
    public VeiculoDto obterVeiculoPorId(Long id) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(id);
        return veiculoDtoMapper.map(veiculo.orElseThrow(() -> new NotFoundException("Veículo não encontrado")));
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
        var novoVeiculo = veiculoDtoMapper.map(veiculoDto, marca);

        return veiculoDtoMapper.map(veiculoRepository.save(novoVeiculo));
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
        var veiculoEncontrado = veiculoDtoMapper.map(obterVeiculoPorId(id), marca);

        veiculoEncontrado.setModelo(veiculoDto.getModelo());
        veiculoEncontrado.setAno(veiculoDto.getAno());
        veiculoEncontrado.setValor(veiculoDto.getValor());
        veiculoEncontrado.setMarca(marca);

        return veiculoDtoMapper.map(veiculoRepository.save(veiculoEncontrado));
    }

    /**
     * Deleta um veículo
     *
     * @param id o id do veículo a ser deletado
     * @return o veículo que foi deletado do banco de dados
     */
    @Transactional
    public VeiculoDto deletarVeiculo(Long id) {
        var veiculoEncontrado = obterVeiculoPorId(id);
        veiculoRepository.deleteById(veiculoEncontrado.getId());
        return veiculoEncontrado;
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
}
