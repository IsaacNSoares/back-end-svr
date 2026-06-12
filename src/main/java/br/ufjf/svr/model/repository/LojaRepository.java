package br.ufjf.svr.model.repository;

import br.ufjf.svr.model.entity.Loja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LojaRepository extends JpaRepository<Loja, Long> {
}
