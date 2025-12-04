package br.fazu.produtos.controller;

import br.fazu.produtos.model.Produto;
import br.fazu.produtos.model.Usuario;
import br.fazu.produtos.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoRepository repository;
    private final String uploadDir = "uploads/";

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Produto> listar() {
        List<Produto> produtos = repository.findAll();

        // Para garantir que a categoria seja carregada corretamente
        for (Produto produto : produtos) {
            // Aqui, você pode definir qual é a categoria, se não estiver carregada automaticamente
            // Caso a relação entre Produto e Categoria seja feita com @ManyToOne, por exemplo, a categoria já virá.
            if (produto.getCategoria() != null) {
                produto.getCategoria().getNome(); // Isso é só para garantir que a categoria será carregada
            }
        }

        return produtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Produto criar(@RequestBody Produto produto) {
        return repository.save(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Integer id, @RequestBody Produto novo) {
        return repository.findById(id)
                .map(produto -> {
                    produto.setNome(novo.getNome());
                    produto.setDescricao(novo.getDescricao());
                    produto.setPreco(novo.getPreco());
                    produto.setEstoque(novo.getEstoque());

                    // FICHA TÉCNICA
                    produto.setPotenciaCV(novo.getPotenciaCV());
                    produto.setAnoFabricacao(novo.getAnoFabricacao());
                    produto.setPesoKG(novo.getPesoKG());
                    produto.setMotor(novo.getMotor());
                    produto.setTransmissao(novo.getTransmissao());
                    produto.setMarchas(novo.getMarchas());
                    produto.setCombustivel(novo.getCombustivel());
                    produto.setEixos(novo.getEixos());
                    produto.setLargura(novo.getLargura());
                    produto.setAltura(novo.getAltura());
                    produto.setComprimento(novo.getComprimento());
                    produto.setFabricante(novo.getFabricante());

                    repository.save(produto);
                    return ResponseEntity.ok(produto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ===========================
    // UPLOAD DE FOTO
    // ===========================
    @PostMapping("/{id}/upload-foto")
    public ResponseEntity<String> uploadFoto(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile arquivo) throws IOException {

        Produto produto = repository.findById(id)
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
        produto.setFoto("/uploads/" + fileName);
        repository.save(produto);

        return ResponseEntity.ok("Foto enviada com sucesso!");
    }

    // ===========================
    // RETORNAR A FOTO COMO ARQUIVO
    // ===========================
    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> getFoto(@PathVariable Integer id) throws IOException {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (produto.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }

        String caminho = produto.getFoto().replace("/uploads/", "");
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
    public ResponseEntity<String> getFotoUrl(@PathVariable Integer id) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (produto.getFoto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(produto.getFoto());
    }
}

