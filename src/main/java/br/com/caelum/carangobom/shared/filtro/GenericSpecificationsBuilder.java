package br.com.caelum.carangobom.shared.filtro;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builder de especificacões do JPA, essa classe é utilizada para realizar filtros de busca no banco de dados
 *
 * @param <T> o tipo do objeto a ser especificado
 */
public class GenericSpecificationsBuilder<T> {

    private final List<SearchCriteria> params;
    private final List<Specification<T>> specifications;

    public GenericSpecificationsBuilder() {
        this.params = new ArrayList<>();
        this.specifications = new ArrayList<>();
    }

    /**
     * Adiciona um operador na especificação
     *
     * @param key a chave da operação a ser adicionada
     * @param searchOperation a operação
     * @param arguments os argumentos utilizados na operação
     * @return um builder com a especificação adicionada
     */
    public final GenericSpecificationsBuilder<T> with(final String key, final SearchCriteria.SearchOperation searchOperation, final List<Object> arguments) {
        return with(key, searchOperation, false, arguments);
    }

    /**
     * Adiciona um operador na especificação
     *
     * @param key a chave da operação a ser adicionada
     * @param searchOperation a operação
     * @param isOrOperation define se é um OR ou AND na operação quando conglomerar com as outras
     * @param arguments os argumentos utilizados na operação
     * @return um builder com a especificação adicionada
     */
    public final GenericSpecificationsBuilder<T> with(final String key, final SearchCriteria.SearchOperation searchOperation, final boolean isOrOperation, final List<Object> arguments) {
        params.add(new SearchCriteria(key, searchOperation, isOrOperation, arguments));
        return this;
    }

    /**
     * Adiciona um operador na especificação utilizando um outra especificação
     *
     * @param specification a especificação a ser adicionada
     * @return um builder com a especificação adicionada
     */
    public final GenericSpecificationsBuilder<T> with(Specification<T> specification) {
        specifications.add(specification);
        return this;
    }

    /**
     * Constrói a especificação utilizando os valores dados, essa epecificação será utilizada quando recuperar os valores
     * do banco de dados.
     *
     * @return a especificação da consulta
     */
    public Specification<T> build() {
        Specification<T> result = null;
        if (!params.isEmpty()) {
            result = new GenericSpecification<>(params.get(0));
            for (var index = 1; index < params.size(); ++index) {
                SearchCriteria searchCriteria = params.get(index);
                result = searchCriteria.isOrOperation() ? Specification.where(result).or(new GenericSpecification<>(searchCriteria))
                        : Specification.where(result).and(new GenericSpecification<>(searchCriteria));
            }
        }
        if (!specifications.isEmpty()) {
            var index = 0;
            if (Objects.isNull(result)) {
                result = specifications.get(index++);
            }
            for (; index < specifications.size(); ++index) {
                result = Specification.where(result).and(specifications.get(index));
            }
        }
        return result;
    }
}
