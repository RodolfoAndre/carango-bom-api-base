package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.shared.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VeiculoController extends GenericController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<List<VeiculoDto>> listarVeiculos() { return encapsulaResultadoOk(veiculoService::listarVeiculos); }

    @GetMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> obterVeiculoPorId(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> veiculoService.obterVeiculoPorId(id));
    }

    @DeleteMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> deletarVeiculo(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> veiculoService.deletarVeiculo(id));
    }
}
