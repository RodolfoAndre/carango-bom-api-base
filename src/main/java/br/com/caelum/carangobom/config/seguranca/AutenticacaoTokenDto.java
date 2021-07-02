package br.com.caelum.carangobom.config.seguranca;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AutenticacaoTokenDto {

    private String token;

    private String tipo;
}
