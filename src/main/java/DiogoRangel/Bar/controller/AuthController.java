package DiogoRangel.Bar.controller;

import DiogoRangel.Bar.dto.LoginDTO;
import DiogoRangel.Bar.exception.DadosInvalidos;
import DiogoRangel.Bar.exception.ItemInexistente;
import DiogoRangel.Bar.model.Usuario;
import DiogoRangel.Bar.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework. web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Endpoint de login para Garçom e Admin
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {

        // Buscar usuário pelo ID
        Usuario usuario = usuarioRepository.findById(loginDTO.getId())
                .orElseThrow(() -> new ItemInexistente("Usuário com ID " + loginDTO.getId() + " não encontrado"));

        // Validar senha
        if (!usuario.getPassword().equals(loginDTO.getSenha())) {
            throw new DadosInvalidos("Senha incorreta para o usuário ID: " + loginDTO.getId());
        }

        // Retornar dados do usuário autenticado
        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nome", usuario.getUsername());
        response.put("perfil", usuario.getPerfil(). toString());
        response.put("message", "Login realizado com sucesso!");

        return ResponseEntity.ok(response);
    }
}