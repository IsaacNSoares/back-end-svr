package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Colaborador;
import br.ufjf.svr.model.repository.ColaboradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ColaboradorService {

    private final ColaboradorRepository repository;

    public ColaboradorService(ColaboradorRepository repository) {
        this.repository = repository;
    }

    public List<Colaborador> getColaboradores() {
        return repository.findAll();
    }

    public Optional<Colaborador> getColaboradorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Colaborador salvar(Colaborador colaborador) {
        validar(colaborador);
        return repository.save(colaborador);
    }

    @Transactional
    public void excluir(Colaborador colaborador) {
        Objects.requireNonNull(colaborador.getId());
        repository.delete(colaborador);
    }

    public void validar(Colaborador colaborador) {
        if (colaborador.getNome() == null || colaborador.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (colaborador.getDocumento() == null || colaborador.getDocumento().trim().equals("")) {
            throw new RegraNegocioException("Documento inválido");
        }
        if (colaborador.getEmail() == null || colaborador.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
        if (colaborador.getCodigo() == null || colaborador.getCodigo().trim().equals("")) {
            throw new RegraNegocioException("Código inválido");
        }
        if (colaborador.getLoja() == null || colaborador.getLoja().getId() == null) {
            throw new RegraNegocioException("Loja inválida");
        }
    }
}