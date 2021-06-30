package br.com.caelum.carangobom.validacao;

import java.util.List;

public class ListaDeErrosOutputDto {

    private List<ErroDeParametroOutputDto> erros;

    public int getQuantidadeDeErros() {
        return erros.size();
    }

    public List<ErroDeParametroOutputDto> getErros() {
        return erros;
    }

    public void setErros(List<ErroDeParametroOutputDto> erros) {
        this.erros = erros;
    }
}
