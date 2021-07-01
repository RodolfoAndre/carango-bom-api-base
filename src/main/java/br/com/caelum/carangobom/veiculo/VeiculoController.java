package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.shared.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.util.List;

/**
 * Classe reponsável pelo controller de veículo.
 */
@CrossOrigin
@Controller
public class VeiculoController extends GenericController {

    private final VeiculoService veiculoService;

    /**
     * Construtor da classe Veículo Controller
     *
     * @param veiculoService o serviço de veículos
     */
    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    /**
     * Lista todas os veículos registrados pelos usuários
     *
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 e os veículos disponíveis em seu "body"
     */
    @GetMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<List<VeiculoDto>> listarVeiculos() { return encapsulaResultadoOk(veiculoService::listarVeiculos); }

    /**
     * Obtém veículos por id
     *
     * @param id o id do veículo a ser obtido
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok) e os veículos disponíveis em seu "body", caso não seja, retornará "status code" 404 (not found).
     */
    @GetMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> obterVeiculoPorId(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> veiculoService.obterVeiculoPorId(id));
    }

    /**
     * Cadastra o veículo dado
     *
     * @param veiculoDto os dados do veículo a ser cadastrado
     * @param uriBuilder o construtor de uri para ser enviado como resposta em caso de sucesso
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PostMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<VeiculoDto> cadastrarVeiculo(@Valid @RequestBody VeiculoDto veiculoDto, UriComponentsBuilder uriBuilder) {
        return encapsularResultadoCreated(() -> veiculoService.cadastrarVeiculo(veiculoDto), uriBuilder, "/veiculos/{id}");
    }

    /**
     * Altera um veículo
     *
     * @param id o id do veículo a ser alterado
     * @param veiculoDto os novos dados do veículo a ser alterado
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PutMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> alterarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoDto veiculoDto) {
        return encapsulaResultadoOk(() -> veiculoService.alterarVeiculo(id, veiculoDto));
    }

    /**
     * Deleta veículos pelo id
     *
     * @param id o id do veículo a ser deletado
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @DeleteMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> deletarVeiculo(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> veiculoService.deletarVeiculo(id));
    }
}
