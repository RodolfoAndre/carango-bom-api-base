package br.com.caelum.carangobom.usuario;

import org.springframework.stereotype.Component;

@Component
public class UsuarioDtoMapper {

    /**
     * Mapeia um {@link UsuarioDto} para um {@link Usuario}
     *
     * @param source o usuário a ser convertido
     * @return a representação do objeto em formato de {@link Usuario}
     */
    public Usuario map(UsuarioDto source) {
        return new Usuario(source.getId(), source.getNome(), source.getSenha(), null);
    }

    /**
     * Mapeia uma {@link Usuario} para uma {@link UsuarioDto}
     *
     * @param source o usuário a ser convertido
     * @return a representação do objeto em formato de {@link UsuarioDto}
     */
    public UsuarioDto map(Usuario source) {
        return new UsuarioDto(source.getId(), source.getNome());
    }
}
