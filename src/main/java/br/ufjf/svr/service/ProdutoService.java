package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Produto;
import br.ufjf.svr.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> getProdutos() {
        return repository.findAll();
    }

    public Optional<Produto> getProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Produto produto) {
        Objects.requireNonNull(produto.getId(), "O ID do produto não pode ser nulo para exclusão.");
        repository.delete(produto);
    }

    public void validar(Produto produto) {
        if (produto.getSku() == null || produto.getSku().trim().isEmpty()) {
            throw new RegraNegocioException("O SKU do produto é obrigatório.");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do produto é obrigatório.");
        }

        if (produto.getPrecoVarejo() == null || produto.getPrecoVarejo() <= 0) {
            throw new RegraNegocioException("O preço de varejo é obrigatório e deve ser maior que zero.");
        }

        if (produto.getPrecoAtacado() == null || produto.getPrecoAtacado() <= 0) {
            throw new RegraNegocioException("O preço de atacado é obrigatório e deve ser maior que zero.");
        }

        if (produto.getFornecedor() == null || produto.getFornecedor().getId() == null) {
            throw new RegraNegocioException("O produto deve estar obrigatoriamente vinculado a um fornecedor.");
        }
    }
}