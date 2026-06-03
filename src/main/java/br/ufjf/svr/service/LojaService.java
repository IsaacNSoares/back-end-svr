package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Loja;
import br.ufjf.svr.model.repository.LojaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LojaService {

    private final LojaRepository repository;

    public LojaService(LojaRepository repository) {
        this.repository = repository;
    }

    public List<Loja> getLojas() {
        return repository.findAll();
    }

    public Optional<Loja> getLojaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Loja salvar(Loja loja) {
        validar(loja);
        return repository.save(loja);
    }

    @Transactional
    public void excluir(Loja loja) {
        Objects.requireNonNull(loja.getId(), "O ID da loja não pode ser nulo para exclusão");
        repository.delete(loja);
    }

    public void validar(Loja loja) {
        if (loja.getNome() == null || loja.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome da loja é obrigatório e não pode estar em branco.");
        }

        if (loja.getCnpj() == null || loja.getCnpj().trim().isEmpty()) {
            throw new RegraNegocioException("O CNPJ da loja é obrigatório.");
        }

        if (loja.getEndereco() == null) {
            throw new RegraNegocioException("A loja deve possuir um endereço vinculado.");
        }
    }
}