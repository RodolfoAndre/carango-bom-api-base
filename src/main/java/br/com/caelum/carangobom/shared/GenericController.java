package br.com.caelum.carangobom.shared;

import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Supplier;

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
}
