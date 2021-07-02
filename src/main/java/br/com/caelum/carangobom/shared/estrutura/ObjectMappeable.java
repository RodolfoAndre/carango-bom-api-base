package br.com.caelum.carangobom.shared.estrutura;

public interface ObjectMappeable<T extends BasicEntity, S extends BasicEntityDto> {

    /**
     * Converte uma entidade para dto
     *
     * @param entidade a entidade a ser convertida
     * @return a representação da entidade em formato de dto
     */
    S converterParaDto(T entidade);

    /**
     * Converte um dto para entidade
     *
     * @param dto o objeto de transferência a ser convertido
     * @return a representação do objeto de transferência em forma de entidade
     */
    T converterParaEntidade(S dto);

}
