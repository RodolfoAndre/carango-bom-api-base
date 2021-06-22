package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    private MarcaRepository marcaRepository;

    private MarcaDtoMapper marcaDtoMapper;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Transactional
    public List<Marca> listarMarcas() {
        return marcaRepository.findAllByOrderByNome();
    }

    @Transactional
    public Marca obterMarcaPorId(Long id) {
        Optional<Marca> marca = marcaRepository.findById(id);
        return marca.orElseThrow(() -> new NotFoundException("Marca não encontrada"));
    }

    @Transactional
    public Marca cadastrarMarca(MarcaDto marcaDto) {
        var marca = marcaDtoMapper.map(marcaDto);
        return marcaRepository.save(marca);
    }

    @Transactional
    public Marca alterarMarca(Long id, MarcaDto marcaDto) {
        var marcaEncontrada = obterMarcaPorId(id);
        marcaEncontrada.setNome(marcaDto.getNome());
        return marcaEncontrada;
    }

    @Transactional
    public Marca deletarMarca(Long id) {
        var marcaEncontrada = obterMarcaPorId(id);
        marcaRepository.delete(marcaEncontrada);
        return marcaEncontrada;
    }
}