package br.com.caelum.carangobom.veiculo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class VeiculoFiltroDto {

    private Set<String> marcas;

    private Set<String> modelos;

    private Double precoMinimo;

    private Double precoMaximo;

}
