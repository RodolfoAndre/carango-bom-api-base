package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável pela lógica de negócios de marcas
 */
@Service
public class MarcaService {

    private MarcaRepository marcaRepository;

    private MarcaDtoMapper marcaDtoMapper;

    /**
     * Construtor de marca service
     *
     * @param marcaRepository o repositório de marcas
     * @param marcaDtoMapper o conversor de objeto de transferência para entidade
     */
    @Autowired
    public MarcaService(MarcaRepository marcaRepository, MarcaDtoMapper marcaDtoMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaDtoMapper = marcaDtoMapper;
    }

    /**
     * Lista todas as marcas que estão presentes no banco de dados
     *
     * @return uma {@link List} de {@link MarcaDto}
     */
    @Transactional
    public List<MarcaDto> listarMarcas() {
        return marcaRepository.findAllByOrderByNome().stream().map(marcaDtoMapper::map).collect(Collectors.toList());
    }

    /**
     * Obtém marcas por id
     *
     * @param id o id da marca a ser obtida
     * @return a {@link MarcaDto} encontrada
     */
    @Transactional
    public MarcaDto obterMarcaPorId(Long id) {
        Optional<Marca> marca = marcaRepository.findById(id);
        return marcaDtoMapper.map(marca.orElseThrow(() -> new NotFoundException("Marca não encontrada")));
    }

    /**
     * Cadastra uma marca
     *
     * @param marcaDto a marca a ser cadastrada
     * @return a marca cadastrada no banco de dados
     */
    @Transactional
    public MarcaDto cadastrarMarca(MarcaDto marcaDto){
        validarMarcaExistente(marcaDto.getNome());

        var novaMarca = marcaDtoMapper.map(marcaDto);
        return marcaDtoMapper.map(marcaRepository.save(novaMarca));
    }

    /**
     * Altera uma marca
     *
     * @param id o id da marca a ser alterada
     * @param marcaDto os novos valores da marca
     * @return a marca alterada no banco de dados
     */
    @Transactional
    public MarcaDto alterarMarca(Long id, MarcaDto marcaDto) {
        validarMarcaExistente(marcaDto.getNome());

        var marcaEncontrada = obterMarcaPorId(id);
        marcaEncontrada.setNome(marcaDto.getNome());
        return marcaEncontrada;
    }

    /**
     * Deleta uma marca
     *
     * @param id o id da marca a ser deletada
     * @return a marca que foi deletada do banco de dados
     */
    @Transactional
    public MarcaDto deletarMarca(Long id) {
        var marcaEncontrada = obterMarcaPorId(id);
        marcaRepository.deleteById(marcaEncontrada.getId());
        return marcaEncontrada;
    }

    /**
     * Valida se uma marca com o nome dado já existe no banco de dados
     *
     * @param nomeMarca o nome da marca a ser verificada
     */
    private void validarMarcaExistente(String nomeMarca) {
        Optional<Marca> marcaEncontrada = marcaRepository.findByNome(nomeMarca);
        marcaEncontrada.ifPresent((m) ->{ throw new ConflictException("Marca " + m.getNome()+ " já existente");});
    }
}