package br.com.caelum.carangobom.marca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    private MarcaRepository marcaRepository;

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
        return marca.orElseThrow(() -> new RuntimeException("Marca não encontrada"));
    }

    @Transactional
    public Marca cadastrarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Transactional
    public Marca alterarMarca(Long id, Marca marca) {
        Marca marcaEncontrada = obterMarcaPorId(id);
        marcaEncontrada.setNome(marca.getNome());
        return marcaEncontrada;
    }

    @Transactional
    public Marca deletarMarca(Long id) {
        Marca marcaEncontrada = obterMarcaPorId(id);
        marcaRepository.delete(marcaEncontrada);
        return marcaEncontrada;
    }

}