package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.shared.estrutura.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * Classe reponsável pelo controller de usuario.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController implements GenericController {

    private final UsuarioService usuarioService;

    /**
     * Construtor da classe Usuário Controller
     *
     * @param usuarioService o serviço de usuarios
     */
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todas as usuários registrados
     *
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 e as usuários disponíveis em seu "body"
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        return encapsulaResultadoOk(usuarioService::listar);
    }

    /**
     * Obtém usuários por id
     *
     * @param id o id da usuário a ser obtida
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok) e as usuários disponíveis em seu "body", caso não seja, retornará "status code" 404 (not found).
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioDto> obterUsuarioPorId(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> usuarioService.obterPorId(id));
    }

    /**
     * Cadastra o usuário dado
     *
     * @param usuarioDto os dados da usuário a ser cadastrado
     * @param uriBuilder o construtor de uri para ser enviado como resposta em caso de sucesso
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<UsuarioDto> cadastrarUsuario(@Valid @RequestBody UsuarioDto usuarioDto, UriComponentsBuilder uriBuilder) {
        return encapsularResultadoCreated(() -> usuarioService.cadastrarUsuario(usuarioDto), uriBuilder, "/usuarios/{id}");
    }

    /**
     * Altera um usuário
     *
     * @param id o id do usuário a ser alterado
     * @param usuarioDto os novos dados do usuário a ser alterado
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioDto> alterarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDto usuarioDto) {
        return encapsulaResultadoOk(() -> usuarioService.alterarUsuario(id, usuarioDto));
    }

    /**
     * Deleta usuarios pelo id
     *
     * @param id o id do usuario a ser deletado
     * @return {@link ResponseEntity} com o resultado da requisição. Caso ocorra tudo como esperado, deverá retornar
     * com "status code" 200 (ok), caso não seja, retornará "status code" 404 (not found).
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<UsuarioDto> deletarUsuario(@PathVariable Long id) {
        return encapsulaResultadoOk(() -> usuarioService.deletar(id));
    }
}