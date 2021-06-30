package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Veiculo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String modelo;
    private Integer ano;
    private Double valor;
    @ManyToOne
    private Marca marca;

    public Veiculo() {

    }

    public Veiculo(String modelo, Integer ano, Double valor, Marca marca) {
        this(null, modelo, ano, valor, marca);
    }

    public Veiculo(Long id, String modelo, Integer ano, Double valor, Marca marca) {
        this.id = id;
        this.modelo = modelo;
        this.ano = ano;
        this.valor = valor;
        this.marca = marca;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}
