package br.ufjf.svr.model.repository;

import br.ufjf.svr.api.dto.CorProdutoDTO;
import br.ufjf.svr.model.entity.CorProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorProdutoRepository extends JpaRepository<CorProduto, Long> {
}
