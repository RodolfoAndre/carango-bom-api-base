package br.com.caelum.carangobom.validacao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroDeParametroOutputDto {

    private String parametro;
    private String mensagem;
}
