package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.shared.estrutura.GenericController;
import br.com.caelum.carangobom.veiculo.dashboard.SumarioMarcaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Classe reponsável pelo controller de veículo.
 */
@Controller
@RequestMapping("/veiculos")
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
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<VeiculoDto>> listarVeiculos() { return encapsulaResultadoOk(veiculoService::listar); }

    /**
     * Obtém veículos por id
     *
     * @param id o id do veículo a ser obtido
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok) e os veículos disponíveis em seu "body", caso não seja, retornará "status code" 404 (not found).
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> obterVeiculoPorId(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> veiculoService.obterPorId(id));
    }

    /**
     * Cadastra o veículo dado
     *
     * @param veiculoDto os dados do veículo a ser cadastrado
     * @param uriBuilder o construtor de uri para ser enviado como resposta em caso de sucesso
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PostMapping
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
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<VeiculoDto> deletarVeiculo(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> veiculoService.deletar(id));
    }

    /**
     * Filtra veículos por marca, modelo e valor
     *
     * @param filtroDto os filtros a serem aplicados na consulta
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok).
     */
    @GetMapping("/filtro")
    @ResponseBody
    public ResponseEntity<List<VeiculoDto>> filtrarVeiculos(@RequestBody VeiculoFiltroDto filtroDto) {
        return encapsulaResultadoOk(() -> veiculoService.filtrarVeiculos(filtroDto));
    }

    /**
     * Recupera o dashboard de veículos
     *
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok).
     */
    @GetMapping("/dashboard")
    @ResponseBody
    public ResponseEntity<Set<SumarioMarcaDto>> dashboard() {
        return encapsulaResultadoOk(veiculoService::dashboard);
    }
}
