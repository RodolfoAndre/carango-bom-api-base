package br.com.caelum.carangobom.shared.filtro;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Uma factory de especificação para auxiliar a criação de especificações de consultas
 *
 * @param <T> o tipo do objeto a ser utilizado na especificação de consulta
 */
@Component
public class SpecificationFactory<T> {

    /**
     * Constrói uma especificação de comparação "maior que" utilizando a chave e valor dado.
     *
     * @param key o campo (coluna) a ser verificada
     * @param arg o valor que o resultado deve trazer maior que
     * @return a especificação dessa operação
     */
    public Specification<T> isGreaterThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchCriteria.SearchOperation.GREATER_THAN, Collections.singletonList(arg)).build();
    }

    /**
     * Constrói uma especificação de comparação "menor que" utilizando a chave e valor dado.
     *
     * @param key o campo (coluna) a ser verificada
     * @param arg o valor que o resultado deve trazer menor que
     * @return a especificação dessa operação
     */
    public Specification<T> isLessThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchCriteria.SearchOperation.LESS_THAN, Collections.singletonList(arg)).build();
    }

    /**
     * Constrói uma especificação de comparação "contém" utilizando a chave e valor dado.
     *
     * @param key o campo (coluna) a ser verificada
     * @param arg o valor que o resultado deve trazer contido no campo da chave dada
     * @return a especificação dessa operação
     */
    public Specification<T> in(String key, Object arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchCriteria.SearchOperation.IN, Collections.singletonList(arg)).build();
    }
}