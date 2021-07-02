package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.shared.estrutura.ObjectMappeable;
import org.springframework.stereotype.Component;

/**
 * Conversor de tipos de objeto marca
 */
@Component
public class MarcaDtoMapper implements ObjectMappeable<Marca, MarcaDto> {

    /**
     * Mapeia uma {@link MarcaDto} para uma {@link Marca}
     *
     * @param source a marca a ser convertida
     * @return a representação do objeto em formato de {@link Marca}
     */
    public Marca converterParaEntidade(MarcaDto source) {
        return new Marca(source.getId(), source.getNome());
    }

    /**
     * Mapeia uma {@link Marca} para uma {@link MarcaDto}
     *
     * @param source a marca a ser convertida
     * @return a representação do objeto em formato de {@link MarcaDto}
     */
    public MarcaDto converterParaDto(Marca source) {
        return new MarcaDto(source.getId(), source.getNome());
    }
}
