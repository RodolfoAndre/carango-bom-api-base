package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.shared.estrutura.BasicEntityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDto extends BasicEntityDto {

    private String nome;

    private String senha;

    public UsuarioDto(Long id, String nome) {
        super.id = id;
        this.nome = nome;
    }

    public UsuarioDto(Long id, String nome, String senha) {
        super.id = id;
        this.nome = nome;
        this.senha = senha;
    }
}
