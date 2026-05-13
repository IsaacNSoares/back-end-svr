package br.ufjf.svr.model.repository;

import br.ufjf.svr.model.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository <Colaborador, Long> {
}
