package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.shared.GenericController;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Classe reponsável pelo controller de marca.
 */
@CrossOrigin
@Controller
public class MarcaController extends GenericController {

    private final MarcaService marcaService;

    /**
     * Construtor da classe Marca Controller
     *
     * @param marcaService o serviço de marcas
     */
    @Autowired
    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    /**
     * Lista todas as marcas registradas pelos usuários
     *
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 e as marcas disponíveis em seu "body"
     */
    @GetMapping("/marcas")
    @ResponseBody
    public ResponseEntity<List<MarcaDto>> listarMarcas() {
        return encapsulaResultadoOk(marcaService::listarMarcas);
    }

    /**
     * Obtém marcas por id
     *
     * @param id o id da marca a ser obtida
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok) e as marcas disponíveis em seu "body", caso não seja, retornará "status code" 404 (not found).
     */
    @GetMapping("/marcas/{id}")
    @ResponseBody
    public ResponseEntity<MarcaDto> obterMarcaPorId(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> marcaService.obterMarcaPorId(id));
    }

    /**
     * Cadastra a marca dada
     *
     * @param marcaDto os dados da marca a ser cadastrada
     * @param uriBuilder o construtor de uri para ser enviado como resposta em caso de sucesso
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PostMapping("/marcas")
    @ResponseBody
    public ResponseEntity<MarcaDto> cadastrarMarca(@Valid @RequestBody MarcaDto marcaDto, UriComponentsBuilder uriBuilder) {
        return encapsularResultadoCreated(() -> marcaService.cadastrarMarca(marcaDto), uriBuilder, "/marcas/{id}");
    }

    /**
     * Altera uma marca
     *
     * @param id o id da marca a ser alterada
     * @param marcaDto os novos dados da marca a ser alterada
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PutMapping("/marcas/{id}")
    @ResponseBody
    public ResponseEntity<MarcaDto> alterarMarca(@PathVariable Long id, @Valid @RequestBody MarcaDto marcaDto) {
        return encapsulaResultadoOk(() -> marcaService.alterarMarca(id, marcaDto));
    }

    /**
     * Deleta marcas pelo id
     *
     * @param id o id da marca a ser deletada
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @DeleteMapping("/marcas/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<MarcaDto> deletarMarca(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> marcaService.deletarMarca(id));
    }
}