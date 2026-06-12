package br.ufjf.svr.model.repository;

import br.ufjf.svr.model.entity.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoPagamentoRepository extends JpaRepository<MetodoPagamento, Long> {
}
