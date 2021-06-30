package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<VeiculoDto> listarVeiculos() {
        return veiculoRepository.findAllByOrderByModelo().stream().map(veiculoDtoMapper::map).collect(Collectors.toList());
    }

    @Transactional
    public VeiculoDto obterVeiculoPorId(Long id) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(id);
        return veiculoDtoMapper.map(veiculo.orElseThrow(() -> new NotFoundException("Veículo não encontrado")));
    }

    @Transactional
    public VeiculoDto deletarVeiculo(Long id) {
        var veiculoEncontrado = obterVeiculoPorId(id);
        veiculoRepository.deleteById(veiculoEncontrado.getId());
        return veiculoEncontrado;
    }
}
