package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaDto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class VeiculoDto {

    private Long id;

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
    private MarcaDto marca;

    public VeiculoDto(Long id, String modelo, Integer ano, Double valor, MarcaDto marca) {
        this.id = id;
        this.modelo = modelo;
        this.ano = ano;
        this.valor = valor;
        this.marca = marca;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }

    public void setAno(Integer ano) { this.ano = ano; }

    public Double getValor() { return valor; }

    public void setValor(Double valor) { this.valor = valor; }

    public MarcaDto getMarca() { return marca; }

    public void setMarca(MarcaDto marca) { this.marca = marca; }
}
