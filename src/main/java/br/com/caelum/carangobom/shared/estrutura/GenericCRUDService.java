package br.com.caelum.carangobom.shared.estrutura;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe abstrata de serviço contendo as implementações padrões de criação, leitura, escrita e deleção
 *
 * @param <T> o tipo da entidade
 * @param <S> o tipo do objeto de transferência
 */
public abstract class GenericCRUDService<T extends BasicEntity, S extends BasicEntityDto> {

    protected final CrudRepository<T, Long> repositorio;

    protected final ObjectMappeable<T, S> mapper;

    /**
     * Construtor padrão
     *
     * @param repositorio o repositório das entidades
     * @param mapper o mapeador de objeto de transferência e objeto de entidade
     */
    protected GenericCRUDService(CrudRepository<T, Long> repositorio, ObjectMappeable<T, S> mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    /**
     * Traz a lista de {@link S} salvo no banco
     *
     * @return uma lista de {@link S}
     */
    @Transactional
    public List<S> listar() {
        List<S> elements = new ArrayList<>();
        repositorio.findAll().iterator().forEachRemaining(e -> elements.add(mapper.converterParaDto(e)));
        return elements;
    }

    /**
     * Obtém {@link S} por id
     *
     * @param id o id da entidade a ser buscada
     * @return a entidade {@link S} encontrada convertida em objeto de transferência
     */
    @Transactional
    public S obterPorId(Long id) {
        Optional<T> entidadeEncontrada = repositorio.findById(id);
        return mapper.converterParaDto(entidadeEncontrada.orElseThrow(() -> new NotFoundException("Entidade não encontrada")));
    }

    /**
     * Salva a entidade do tipo {@link S} no banco de dados
     *
     * @param objetoEntidade a entidade a ser inserida no banco
     * @return A entidade inserida no banco em formado de objeto de transferência
     */
    @Transactional
    public S salvar(T objetoEntidade) {
        return mapper.converterParaDto(repositorio.save(objetoEntidade));
    }

    /**
     * Salva a entidade do tipo {@link S} no banco de dados
     *
     * @param dto a entidade a ser inserida no banco
     * @return A entidade inserida no banco em formado de objeto de transferência
     */
    @Transactional
    public S salvar(S dto) {
        var entidade = mapper.converterParaEntidade(dto);
        return mapper.converterParaDto(repositorio.save(entidade));
    }

    /**
     * Deleta a entidade {link S} por id
     *
     * @param id o id da entidade {@link S} a ser deletada
     * @return a entidade deletada
     */
    @Transactional
    public S deletar(Long id) {
        var entidadeEncontrada = obterPorId(id);
        repositorio.deleteById(entidadeEncontrada.getId());
        return entidadeEncontrada;
    }
}
