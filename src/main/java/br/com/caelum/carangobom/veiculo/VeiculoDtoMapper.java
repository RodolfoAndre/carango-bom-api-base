package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.shared.estrutura.ObjectMappeable;
import org.springframework.stereotype.Component;

/**
 * Conversor de tipos de objeto veículo
 */
@Component
public class VeiculoDtoMapper implements ObjectMappeable<Veiculo, VeiculoDto> {

    /**
     * Mapeia um {@link VeiculoDto} para um {@link Veiculo}
     *
     * @param dto o veículo a ser convertido
     * @param marca a marca do veículo que será convertido
     * @return a representação do objeto em formato de {@link Veiculo}
     */
    public Veiculo converterParaEntidade(VeiculoDto dto) {
        return new Veiculo(dto.getId(), dto.getModelo(), dto.getAno(), dto.getValor());
    }

    /**
     * Mapeia um {@link Veiculo} para um {@link VeiculoDto}
     *
     * @param entidade o veículo a ser convertido
     * @return a representação do objeto em formato de {@link VeiculoDto}
     */
    public VeiculoDto converterParaDto(Veiculo entidade) {
        return new VeiculoDto(entidade.getId(), entidade.getModelo(), entidade.getAno(), entidade.getValor(), entidade.getMarca().getNome());
    }
}
