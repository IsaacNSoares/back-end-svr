package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.CorProduto;
import br.ufjf.svr.model.repository.CorProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CorProdutoService {

    private final CorProdutoRepository repository;

    public CorProdutoService(CorProdutoRepository repository) {
        this.repository = repository;
    }

    public List<CorProduto> getCorProdutos() {
        return repository.findAll();
    }

    public Optional<CorProduto> getCorProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public CorProduto salvar(CorProduto corProduto) {
        validar(corProduto);
        return repository.save(corProduto);
    }

    @Transactional
    public void excluir(CorProduto corProduto) {
        Objects.requireNonNull(corProduto.getId());
        repository.delete(corProduto);
    }

    public void validar(CorProduto corProduto) {
        if (corProduto.getNome() == null || corProduto.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome da cor inválido");
        }
        if (corProduto.getProduto() == null || corProduto.getProduto().getId() == null) {
            throw new RegraNegocioException("Produto inválido");
        }
    }
}