package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.validacao.ErroDeParametroOutputDto;
import br.com.caelum.carangobom.validacao.ListaDeErrosOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
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
    public List<Marca> lista() {
        return marcaService.listarMarcas();
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
    public ResponseEntity<Marca> cadastra(@Valid @RequestBody MarcaDto marcaDto, UriComponentsBuilder uriBuilder) {
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ListaDeErrosOutputDto validacao(MethodArgumentNotValidException excecao) {
        List<ErroDeParametroOutputDto> listaDeErro = new ArrayList<>();
        excecao.getBindingResult().getFieldErrors().forEach(e -> {
            var erroDeParametro = new ErroDeParametroOutputDto();
            erroDeParametro.setParametro(e.getField());
            erroDeParametro.setMensagem(e.getDefaultMessage());
            listaDeErro.add(erroDeParametro);
        });
        var listaDeErrosOutput = new ListaDeErrosOutputDto();
        listaDeErrosOutput.setErros(listaDeErro);
        return listaDeErrosOutput;
    }
}