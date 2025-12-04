package br.fazu.produtos.controller;

import br.fazu.produtos.model.Usuario;
import br.fazu.produtos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final String uploadDir = "uploads/";
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ===========================
    // LISTAR TODOS
    // ===========================
    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // ===========================
    // BUSCAR POR ID
    // ===========================
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===========================
    // CRIAR
    // ===========================
    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // ===========================
    // EDITAR
    // ===========================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario novo) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(novo.getNome());
                    usuario.setEmail(novo.getEmail());
                    usuario.setSenha(novo.getSenha());
                    usuarioRepository.save(usuario);
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===========================
    // EXCLUIR
    // ===========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ===========================
    // LOGIN
    // ===========================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario, HttpSession session) {
        Usuario user = usuarioRepository.findByEmailAndSenha(usuario.getEmail(), usuario.getSenha());

        if (user != null) {
            session.setAttribute("usuarioLogado", user);
            return ResponseEntity.ok("Login realizado com sucesso!");
        }

        return ResponseEntity.status(401).body("Email ou senha incorretos.");
    }

    // ===========================
    // UPLOAD DE FOTO
    // ===========================
    @PostMapping("/{id}/upload-foto")
    public ResponseEntity<String> uploadFoto(
            @PathVariable Long id,
            @RequestParam("foto") MultipartFile arquivo) throws IOException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Cria diretório se não existir
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Nome do arquivo
        String fileName = "user-" + id + "-" + arquivo.getOriginalFilename();
        File destino = new File(uploadDir + fileName);

        // Salva arquivo
        arquivo.transferTo(destino.toPath());

        // Salva o caminho relativo
        usuario.setFotoPerfil("/uploads/" + fileName);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Foto enviada com sucesso!");
    }

    // ===========================
    // RETORNAR A FOTO COMO ARQUIVO
    // ===========================
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> getFoto(@PathVariable Long id) throws IOException {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        String caminho = usuario.getFotoPerfil().replace("/uploads/", "");
        File file = new File(uploadDir + caminho);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(file.toPath());

        String mimeType = Files.probeContentType(file.toPath());

        return ResponseEntity.ok()
                .header("Content-Type", mimeType != null ? mimeType : "image/jpeg")
                .body(imageBytes);
    }

    // ===========================
    // RETORNAR APENAS A URL DA FOTO
    // ===========================
    @GetMapping("/{id}/foto/url")
    public ResponseEntity<String> getFotoUrl(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario.getFotoPerfil());
    }
}
