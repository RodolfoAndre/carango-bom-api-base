package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.shared.estrutura.BasicEntityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Objeto de transferência de dado usado para marca
 */
@Getter
@Setter
@AllArgsConstructor
public class MarcaDto extends BasicEntityDto {

    @NotBlank
    @Size(min = 2, message = "Deve ter {min} ou mais caracteres.")
    private String nome;

    public MarcaDto(Long id, String nome) {
        super.id = id;
        this.nome = nome;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        MarcaDto marcaDto = (MarcaDto) that;
        return Objects.equals(nome, marcaDto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "MarcaDto{" +
                "nome='" + nome + '\'' +
                ", id=" + id +
                '}';
    }
}
