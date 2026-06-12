package br.ufjf.svr.model.repository;

import br.ufjf.svr.model.entity.Pessoa;
import br.ufjf.svr.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
