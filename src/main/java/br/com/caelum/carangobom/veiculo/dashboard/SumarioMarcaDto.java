package br.com.caelum.carangobom.veiculo.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SumarioMarcaDto {

    private String marca;

    private int numeroDeVeiculos;

    private double valorTotal;

    private Set<SumarioModeloDto> modelos;

    public SumarioMarcaDto(String marca) {
        this.marca = marca;
        this.modelos = new HashSet<>();
    }
}
