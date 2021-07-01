package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.MarcaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Classe responsável por gerenciar a entidade veículo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public Veiculo(String modelo, Integer ano, Double valor, Marca marca) {
        this(null, modelo, ano, valor, marca);
    }
}
