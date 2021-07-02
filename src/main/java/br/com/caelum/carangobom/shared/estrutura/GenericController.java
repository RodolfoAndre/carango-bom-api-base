package br.com.caelum.carangobom.shared.estrutura;

import br.com.caelum.carangobom.exception.NotFoundException;
import br.com.caelum.carangobom.validacao.ErroDeParametroOutputDto;
import br.com.caelum.carangobom.validacao.ListaDeErrosOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Classe genérica de controller
 */
public abstract class GenericController {

    /**
     * Encapsula o resultado da função em uma {@link ResponseEntity} Ok(200). Se ocorrer alguma exceção {@link NotFoundException}
     * retornará um {@link ResponseEntity} Not Found(404).
     *
     * @param funcao a função para ser executada
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    public <T> ResponseEntity<T> encapsulaResultadoOk(Supplier<T> funcao) {
        try {
            return ResponseEntity.ok(funcao.get());
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Encapsula o resultado da função em uma {@link ResponseEntity} Created(201). Se ocorrer alguma exceção {@link NotFoundException}
     * retornará um {@link ResponseEntity} Not Found(404).
     *
     * @param funcao a função para ser executada
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    public <T extends BasicEntityDto> ResponseEntity<T> encapsularResultadoCreated(Supplier<T> funcao, UriComponentsBuilder uriBuilder, String url) {
        try {
            var resultado = funcao.get();
            var uri = uriBuilder.path(url).buildAndExpand(resultado.getId()).toUri();
            return ResponseEntity.created(uri).body(resultado);
        } catch (NotFoundException notFoundException) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Valida os parâmetros passados nas requisições de acordo com as regras definidas nos Dto's
     * @param excecao a exceção capturada
     * @return {@link ListaDeErrosOutputDto} com a lista de erros
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ListaDeErrosOutputDto validarParametrosRequisicao(MethodArgumentNotValidException excecao) {
        var listaDeErrosOutput = new ListaDeErrosOutputDto();

        var listaDeErro = montarListaDeErros(excecao);
        listaDeErrosOutput.setErros(listaDeErro);
        return listaDeErrosOutput;
    }

    /**
     * Monta a lista de erros
     * @param excecao a exceção capturada
     * @return {@link List<ErroDeParametroOutputDto>} com a lista de erros
     */
    private List<ErroDeParametroOutputDto> montarListaDeErros(MethodArgumentNotValidException excecao){
        List<ErroDeParametroOutputDto> listaDeErro = new ArrayList<>();
        var resultados = excecao.getBindingResult();
        var errosDeCampo = resultados.getFieldErrors();

        errosDeCampo.forEach(erro -> {
            var erroDeParametro = new ErroDeParametroOutputDto();
            erroDeParametro.setParametro(erro.getField());
            erroDeParametro.setMensagem(erro.getDefaultMessage());
            listaDeErro.add(erroDeParametro);
        });

        return listaDeErro;
    }
}
