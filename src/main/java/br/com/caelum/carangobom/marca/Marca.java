package br.com.caelum.carangobom.marca;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Classe responsável por gerenciar a entidade marca
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Marca {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String nome;

    public Marca(String nome) {
        this(null, nome);
    }
}
