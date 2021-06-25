package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MarcaController {

    private final MarcaService marcaService;

    @Autowired
    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping("/marcas")
    @ResponseBody
    public ResponseEntity<List<Marca>> listarMarcas() {
        return ResponseEntity.ok(marcaService.listarMarcas());
    }

    @GetMapping("/marcas/{id}")
    @ResponseBody
    public ResponseEntity<Marca> obterMarcarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(marcaService.obterMarcaPorId(id));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/marcas")
    @ResponseBody
    public ResponseEntity<Marca> cadastrarMarca(@Valid @RequestBody MarcaDto marcaDto, UriComponentsBuilder uriBuilder) {
        try {
            var marcaSalva = marcaService.cadastrarMarca(marcaDto);
            var uri = uriBuilder.path("/marcas/{id}").buildAndExpand(marcaSalva.getId()).toUri();
            return ResponseEntity.created(uri).body(marcaSalva);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/marcas/{id}")
    @ResponseBody
    public ResponseEntity<Marca> alterarMarca(@PathVariable Long id, @Valid @RequestBody MarcaDto marcaDto) {
        try {
            return ResponseEntity.ok(marcaService.alterarMarca(id, marcaDto));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/marcas/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Marca> deletarMarca(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(marcaService.deletarMarca(id));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }
}