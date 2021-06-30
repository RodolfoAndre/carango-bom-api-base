package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDto;
import org.springframework.stereotype.Component;

@Component
public class VeiculoDtoMapper {

    public Veiculo map(VeiculoDto source, Marca marca) {

        return new Veiculo(source.getId(), source.getModelo(), source.getAno(), source.getValor(), marca);
    }

    public VeiculoDto map(Veiculo source, MarcaDto marca) {
        return new VeiculoDto(source.getId(), source.getModelo(), source.getAno(), source.getValor(), marca);
    }
}
