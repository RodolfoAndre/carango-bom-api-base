package br.com.caelum.carangobom.autenticacao;

import br.com.caelum.carangobom.config.seguranca.AutenticacaoTokenDto;
import br.com.caelum.carangobom.config.seguranca.TokenService;
import br.com.caelum.carangobom.shared.estrutura.GenericController;
import br.com.caelum.carangobom.usuario.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AutenticacaoController extends GenericController {

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<AutenticacaoTokenDto> autenticar(@RequestBody @Valid UsuarioDto usuarioDto) {
        return encapsulaResultadoOk(() -> tokenService.gerarToken(usuarioDto));
    }
}
