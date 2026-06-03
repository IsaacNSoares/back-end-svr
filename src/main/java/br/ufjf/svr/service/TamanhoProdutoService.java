package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.TamanhoProduto;
import br.ufjf.svr.model.repository.TamanhoProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TamanhoProdutoService {

    private final TamanhoProdutoRepository repository;

    public TamanhoProdutoService(TamanhoProdutoRepository repository) {
        this.repository = repository;
    }

    public List<TamanhoProduto> getTamanhos() {
        return repository.findAll();
    }

    public Optional<TamanhoProduto> getTamanhoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public TamanhoProduto salvar(TamanhoProduto tamanhoProduto) {
        validar(tamanhoProduto);
        return repository.save(tamanhoProduto);
    }

    @Transactional
    public void excluir(TamanhoProduto tamanhoProduto) {
        Objects.requireNonNull(tamanhoProduto.getId(), "O ID do tamanho não pode ser nulo para exclusão.");
        repository.delete(tamanhoProduto);
    }

    public void validar(TamanhoProduto tamanhoProduto) {
        if (tamanhoProduto.getNome() == null || tamanhoProduto.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do tamanho é obrigatório (ex: P, M, G, 42).");
        }

        if (tamanhoProduto.getProduto() == null || tamanhoProduto.getProduto().getId() == null) {
            throw new RegraNegocioException("O tamanho deve estar obrigatoriamente vinculado a um produto.");
        }
    }
}