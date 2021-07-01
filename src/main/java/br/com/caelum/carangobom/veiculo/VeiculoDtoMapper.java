package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDto;
import org.springframework.stereotype.Component;

/**
 * Conversor de tipos de objeto veículo
 */
@Component
public class VeiculoDtoMapper {

    /**
     * Mapeia um {@link VeiculoDto} para um {@link Veiculo}
     *
     * @param source o veículo a ser convertido
     * @param marca a marca do veículo que será convertido
     * @return a representação do objeto em formato de {@link Veiculo}
     */
    public Veiculo map(VeiculoDto source, Marca marca) {
        return new Veiculo(source.getId(), source.getModelo(), source.getAno(), source.getValor(), marca);
    }

    /**
     * Mapeia um {@link Veiculo} para um {@link VeiculoDto}
     *
     * @param source a veículo a ser convertido
     * @return a representação do objeto em formato de {@link VeiculoDto}
     */
    public VeiculoDto map(Veiculo source) {
        return new VeiculoDto(source.getId(), source.getModelo(), source.getAno(), source.getValor(), source.getMarca().getNome());
    }
}
