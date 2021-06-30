package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDto;
import br.com.caelum.carangobom.shared.BasicEntityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class VeiculoDto extends BasicEntityDto {

    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String modelo;

    @NotBlank
    @Size(min = 4, max = 4, message = "Deve ter 4 caracteres.")
    private Integer ano;

    @NotBlank
 //   @DecimalMin( , message = "Valor deve ser maior que zero.")
    private Double valor;

    @NotBlank
    private String marca;

    public VeiculoDto(Long id, String modelo, Integer ano, Double valor, String marca) {
        this.id = id;
        this.modelo = modelo;
        this.ano = ano;
        this.valor = valor;
        this.marca = marca;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        VeiculoDto veiculoDto = (VeiculoDto) that;
        return Objects.equals(modelo, veiculoDto.modelo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelo);
    }

    @Override
    public String toString() {
        return "VeiculoDto{" +
                "modelo='" + modelo + '\'' +
                ", marca=" + marca +
                ", ano='" + ano + '\'' +
                ", valor='" + valor + '\'' +
                ", id=" + id +
                '}';
    }
}
