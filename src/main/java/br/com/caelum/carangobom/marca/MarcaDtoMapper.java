package br.com.caelum.carangobom.marca;

import org.springframework.stereotype.Component;

/**
 * Conversor de tipos de objeto marca
 */
@Component
public class MarcaDtoMapper {

    /**
     * Mapeia uma {@link MarcaDto} para uma {@link Marca}
     *
     * @param source a marca a ser convertida
     * @return a representação do objeto em formato de {@link Marca}
     */
    public Marca map(MarcaDto source) {
        return new Marca(source.getId(), source.getNome());
    }

    /**
     * Mapeia uma {@link Marca} para uma {@link MarcaDto}
     *
     * @param source a marca a ser convertida
     * @return a representação do objeto em formato de {@link MarcaDto}
     */
    public MarcaDto map(Marca source) {
        return new MarcaDto(source.getId(), source.getNome());
    }
}
