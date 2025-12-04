package br.fazu.produtos.repository;

import br.fazu.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {}
