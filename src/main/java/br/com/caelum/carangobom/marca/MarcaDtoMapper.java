package br.com.caelum.carangobom.marca;

import org.springframework.stereotype.Component;

@Component
public class MarcaDtoMapper {

    public Marca map(MarcaDto source) {
        return new Marca(source.getId(), source.getNome());
    }
}
