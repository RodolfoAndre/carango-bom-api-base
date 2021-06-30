package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<List<Veiculo>> listarVeiculos() {
        return ResponseEntity.ok(veiculoService.listarVeiculos());
    }

    @GetMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Veiculo> obterVeiculoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(veiculoService.obterVeiculoPorId(id));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Veiculo> deletarVeiculo(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(veiculoService.deletarVeiculo(id));
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }
}
