package br.ufjf.svr.model.repository;

import br.ufjf.svr.model.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
}
