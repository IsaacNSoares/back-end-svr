package br.ufjf.svr.model.repository;

import br.ufjf.svr.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {
}
