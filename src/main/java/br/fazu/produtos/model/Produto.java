package br.fazu.produtos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private Categoria categoria;

    private String foto;
    private Integer potenciaCV;
    private Integer anoFabricacao;
    private Double pesoKG;
    private String motor;
    private String transmissao;
    private String marchas;
    private String combustivel;
    private Integer eixos;
    private Double largura;
    private Double altura;
    private Double comprimento;
    private String fabricante;

    @Column(name = "criado_em", insertable = false, updatable = false)
    private LocalDateTime criadoEm;

    public Produto() {}

    public Produto(Integer id, String nome, String descricao, BigDecimal preco, Integer estoque, Categoria categoria, String foto, Integer potenciaCV, Integer anoFabricacao, Double pesoKG, String motor, String transmissao, String marchas, String combustivel, Integer eixos, Double largura, Double altura, Double comprimento, String fabricante, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.foto = foto;
        this.potenciaCV = potenciaCV;
        this.anoFabricacao = anoFabricacao;
        this.pesoKG = pesoKG;
        this.motor = motor;
        this.transmissao = transmissao;
        this.marchas = marchas;
        this.combustivel = combustivel;
        this.eixos = eixos;
        this.largura = largura;
        this.altura = altura;
        this.comprimento = comprimento;
        this.fabricante = fabricante;
        this.criadoEm = criadoEm;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public void setId(Integer id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public Integer getPotenciaCV() {
        return potenciaCV;
    }

    public void setPotenciaCV(Integer potenciaCV) {
        this.potenciaCV = potenciaCV;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Double getPesoKG() {
        return pesoKG;
    }

    public void setPesoKG(Double pesoKG) {
        this.pesoKG = pesoKG;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getTransmissao() {
        return transmissao;
    }

    public void setTransmissao(String transmissao) {
        this.transmissao = transmissao;
    }

    public String getMarchas() {
        return marchas;
    }

    public void setMarchas(String marchas) {
        this.marchas = marchas;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public Integer getEixos() {
        return eixos;
    }

    public void setEixos(Integer eixos) {
        this.eixos = eixos;
    }

    public Double getLargura() {
        return largura;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public void setComprimento(Double comprimento) {
        this.comprimento = comprimento;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", estoque=" + estoque +
                ", categoria=" + categoria +
                ", criadoEm=" + criadoEm +
                '}';
    }
}
