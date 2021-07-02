package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.shared.estrutura.BasicEntityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Objeto de transferência de dado usado para veículo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDto extends BasicEntityDto {

    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String modelo;

    @Min(value = 1800, message = "Deve ter 4 caracteres.")
    private Integer ano;

    @Min(value = 0, message = "Valor deve ser maior que zero.")
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
