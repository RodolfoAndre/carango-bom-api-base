package br.com.caelum.carangobom.shared.filtro;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Specification<T> and(Specification<T> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Object> arguments = criteria.getArguments();
        Object arg = arguments.get(0);

        switch (criteria.getSearchOperation()) {
            case EQUALITY:
                return criteriaBuilder.equal(root.get(criteria.getKey()), arg);
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(root.get(criteria.getKey()), (Comparable) arg);
            case LESS_THAN:
                return criteriaBuilder.lessThan(root.get(criteria.getKey()), (Comparable) arg);
            case IN:
                return root.get(criteria.getKey()).in(arguments);
            default:
                return null;
        }
    }
}

