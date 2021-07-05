package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.shared.estrutura.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Classe responsável por gerenciar a entidade veículo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Veiculo implements BasicEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String modelo;

    private Integer ano;

    private Double valor;

    @ManyToOne
    private Marca marca;

    public Veiculo(Long id, String modelo, Integer ano, Double valor) {
        this.id = id;
        this.modelo = modelo;
        this.ano = ano;
        this.valor = valor;
    }
}
