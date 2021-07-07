package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.shared.estrutura.GenericCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Classe responsável pela lógica de negócios de marcas
 */
@Service
public class MarcaService extends GenericCRUDService<Marca, MarcaDto> {

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
        super(marcaRepository, marcaDtoMapper);
        this.marcaRepository = marcaRepository;
        this.marcaDtoMapper = marcaDtoMapper;
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

        var novaMarca = marcaDtoMapper.converterParaEntidade(marcaDto);
        return marcaDtoMapper.converterParaDto(marcaRepository.save(novaMarca));
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

        var marcaEncontrada = obterPorId(id);
        marcaEncontrada.setNome(marcaDto.getNome());
        return salvar(marcaEncontrada);
    }

    /**
     * Valida se uma marca com o nome dado já existe no banco de dados
     *
     * @param nomeMarca o nome da marca a ser verificada
     */
    private void validarMarcaExistente(String nomeMarca) {
        Optional<Marca> marcaEncontrada = marcaRepository.findByNome(nomeMarca);
        marcaEncontrada.ifPresent(m ->{ throw new ConflictException("Marca " + m.getNome()+ " já existente");});
    }
}