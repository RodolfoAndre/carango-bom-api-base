package br.com.caelum.carangobom.veiculo.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SumarioModeloDto {

    private String modelo;

    private int numeroDeVeiculos;

    private double valorTotal;

    public SumarioModeloDto(String modelo) {
        this.modelo = modelo;
    }
}
