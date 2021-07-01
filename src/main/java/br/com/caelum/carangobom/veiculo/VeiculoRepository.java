package br.com.caelum.carangobom.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável por gerenciar os dados de veículo
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    List<Veiculo> findAllByOrderByModelo();
}
