package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.validacao.ErroDeParametroOutputDto;
import br.com.caelum.carangobom.validacao.ListaDeErrosOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MarcaController {

    private MarcaRepository marcaRepository;

    @Autowired
    public MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @GetMapping("/marcas")
    @ResponseBody
    @Transactional
    public List<Marca> lista() {
        return marcaRepository.findAllByOrderByNome();
    }

    @GetMapping("/marcas/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Marca> id(@PathVariable Long id) {
        Optional<Marca> m1 = marcaRepository.findById(id);
        if (m1.isPresent()) {
            return ResponseEntity.ok(m1.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/marcas")
    @ResponseBody
    @Transactional
    public ResponseEntity<Marca> cadastra(@Valid @RequestBody Marca m1, UriComponentsBuilder uriBuilder) {
        Marca m2 = marcaRepository.save(m1);
        URI h = uriBuilder.path("/marcas/{id}").buildAndExpand(m1.getId()).toUri();
        return ResponseEntity.created(h).body(m2);
    }

    @PutMapping("/marcas/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Marca> altera(@PathVariable Long id, @Valid @RequestBody Marca m1) {
        Optional<Marca> m2 = marcaRepository.findById(id);
        if (m2.isPresent()) {
            Marca m3 = m2.get();
            m3.setNome(m1.getNome());
            return ResponseEntity.ok(m3);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/marcas/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Marca> deleta(@PathVariable Long id) {
        Optional<Marca> m1 = marcaRepository.findById(id);
        if (m1.isPresent()) {
            Marca m2 = m1.get();
            marcaRepository.delete(m2);
            return ResponseEntity.ok(m2);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ListaDeErrosOutputDto validacao(MethodArgumentNotValidException excecao) {
        List<ErroDeParametroOutputDto> l = new ArrayList<>();
        excecao.getBindingResult().getFieldErrors().forEach(e -> {
            ErroDeParametroOutputDto d = new ErroDeParametroOutputDto();
            d.setParametro(e.getField());
            d.setMensagem(e.getDefaultMessage());
            l.add(d);
        });
        ListaDeErrosOutputDto l2 = new ListaDeErrosOutputDto();
        l2.setErros(l);
        return l2;
    }
}