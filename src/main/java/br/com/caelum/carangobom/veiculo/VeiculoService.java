package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private VeiculoRepository veiculoRepository;

    private VeiculoDtoMapper veiculoDtoMapper;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository, VeiculoDtoMapper veiculoDtoMapper) {
        this.veiculoRepository = veiculoRepository;
        this.veiculoDtoMapper = veiculoDtoMapper;
    }

    @Transactional
    public List<Veiculo> listarVeiculos() {
        return veiculoRepository.findAllByOrderByModelo();
    }

    @Transactional
    public Veiculo obterVeiculoPorId(Long id) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(id);
        return veiculo.orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
    }

    @Transactional
    public Veiculo deletarVeiculo(Long id) {
        var veiculoEncontrado = obterVeiculoPorId(id);
        veiculoRepository.delete(veiculoEncontrado);
        return veiculoEncontrado;
    }
}
