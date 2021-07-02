package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.shared.estrutura.GenericCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Classe responsável pela lógica de negócios de usuarios
 */
@Service
public class UsuarioService extends GenericCRUDService<Usuario, UsuarioDto> {

    private UsuarioRepository usuarioRepository;

    /**
     * Construtor de usuário service
     *
     * @param usuarioRepository o repositório de usuários
     * @param usuarioDtoMapper o conversor de objeto de transferência para entidade
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioDtoMapper usuarioDtoMapper) {
        super(usuarioRepository, usuarioDtoMapper);
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cadastra uma usuario
     *
     * @param usuarioDto a usuario a ser cadastrada
     * @return a usuario cadastrada no banco de dados
     */
    @Transactional
    public UsuarioDto cadastrarUsuario(UsuarioDto usuarioDto){
        validarUsuarioExistente(usuarioDto.getNome());
        String senhaEncriptada = new BCryptPasswordEncoder().encode(usuarioDto.getSenha());
        usuarioDto.setSenha(senhaEncriptada);
        return salvar(usuarioDto);
    }

    /**
     * Altera uma usuario
     *
     * @param id o id da usuario a ser alterada
     * @param usuarioDto os novos valores da usuario
     * @return a usuario alterada no banco de dados
     */
    @Transactional
    public UsuarioDto alterarUsuario(Long id, UsuarioDto usuarioDto) {
        var usuarioEncontrado = obterPorId(id);
        String senhaEncriptada = new BCryptPasswordEncoder().encode(usuarioDto.getSenha());
        usuarioEncontrado.setSenha(senhaEncriptada);
        return salvar(usuarioEncontrado);
    }

    /**
     * Valida se uma usuario com o nome dado já existe no banco de dados
     *
     * @param nomeUsuario o nome da usuario a ser verificada
     */
    private void validarUsuarioExistente(String nomeUsuario) {
        Optional<Usuario> usuarioEncontrada = usuarioRepository.findByNome(nomeUsuario);
        usuarioEncontrada.ifPresent(m ->{ throw new ConflictException("usuario " + m.getNome()+ " já existente");});
    }
}