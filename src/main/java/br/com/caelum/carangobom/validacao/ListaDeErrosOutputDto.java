package br.com.caelum.carangobom.validacao;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListaDeErrosOutputDto {

    private List<ErroDeParametroOutputDto> erros;
}
