package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.ConflictException;
import br.com.caelum.carangobom.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável pela lógica de negócios de usuarios
 */
@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    private UsuarioDtoMapper usuarioDtoMapper;

    /**
     * Construtor de usuário service
     *
     * @param usuarioRepository o repositório de usuários
     * @param usuarioDtoMapper o conversor de objeto de transferência para entidade
     */
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioDtoMapper usuarioDtoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDtoMapper = usuarioDtoMapper;
    }

    /**
     * Lista todas as usuarios que estão presentes no banco de dados
     *
     * @return uma {@link List} de {@link UsuarioDto}
     */
    @Transactional
    public List<UsuarioDto> listarUsuarios() {
        return usuarioRepository.findAllByOrderByNome().stream().map(usuarioDtoMapper::map).collect(Collectors.toList());
    }

    /**
     * Obtém usuarios por id
     *
     * @param id o id da usuario a ser obtida
     * @return a {@link UsuarioDto} encontrada
     */
    @Transactional
    public UsuarioDto obterUsuarioPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuarioDtoMapper.map(usuario.orElseThrow(() -> new NotFoundException("usuario não encontrada")));
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
        var novoUsuario = usuarioDtoMapper.map(usuarioDto);
        return usuarioDtoMapper.map(usuarioRepository.save(novoUsuario));
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
        var usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
        String senhaEncriptada = new BCryptPasswordEncoder().encode(usuarioDto.getSenha());
        usuarioEncontrado.setSenha(senhaEncriptada);
        usuarioRepository.save(usuarioEncontrado);
        return usuarioDtoMapper.map(usuarioEncontrado);
    }

    /**
     * Deleta uma usuario
     *
     * @param id o id da usuario a ser deletada
     * @return a usuario que foi deletada do banco de dados
     */
    @Transactional
    public UsuarioDto deletarUsuario(Long id) {
        var usuarioEncontrada = obterUsuarioPorId(id);
        usuarioRepository.deleteById(usuarioEncontrada.getId());
        return usuarioEncontrada;
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