package br.com.caelum.carangobom.marca;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Classe responsável por gerenciar a entidade marca
 */
@Entity
public class Marca {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nome;

    public Marca() {

    }

    public Marca(String nome) {
        this(null, nome);
    }

    public Marca(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
