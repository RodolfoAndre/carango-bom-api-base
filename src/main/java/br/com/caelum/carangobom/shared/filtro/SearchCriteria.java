package br.com.caelum.carangobom.shared.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SearchCriteria {

    private String key;
    private SearchOperation searchOperation;
    private boolean isOrOperation;
    private List<Object> arguments;

    public enum SearchOperation {
        EQUALITY, GREATER_THAN, LESS_THAN, LIKE, IN
    }
}
