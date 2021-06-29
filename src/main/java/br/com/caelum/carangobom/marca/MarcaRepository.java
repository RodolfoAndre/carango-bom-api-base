package br.com.caelum.carangobom.marca;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável por gerenciar os dados de marca
 */
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNome(String nomeMarca);

    List<Marca> findAllByOrderByNome();
}
