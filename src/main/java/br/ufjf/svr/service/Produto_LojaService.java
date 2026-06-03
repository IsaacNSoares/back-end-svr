package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Produto_Loja;
import br.ufjf.svr.model.repository.Produto_LojaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class Produto_LojaService {

    private final Produto_LojaRepository repository;

    public Produto_LojaService(Produto_LojaRepository repository) {
        this.repository = repository;
    }

    public List<Produto_Loja> getEstoques() {
        return repository.findAll();
    }

    public Optional<Produto_Loja> getEstoqueById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto_Loja salvar(Produto_Loja produtoLoja) {
        validar(produtoLoja);
        return repository.save(produtoLoja);
    }

    @Transactional
    public void excluir(Produto_Loja produtoLoja) {
        Objects.requireNonNull(produtoLoja.getId(), "O ID do registro de estoque não pode ser nulo para exclusão.");
        repository.delete(produtoLoja);
    }

    public void validar(Produto_Loja produtoLoja) {
        if (produtoLoja.getQuantidade() == null || produtoLoja.getQuantidade() < 0) {
            throw new RegraNegocioException("A quantidade em estoque é obrigatória e não pode ser negativa.");
        }

        if (produtoLoja.getLoja() == null || produtoLoja.getLoja().getId() == null) {
            throw new RegraNegocioException("O registro de estoque deve estar obrigatoriamente vinculado a uma Loja.");
        }

        if (produtoLoja.getProduto() == null || produtoLoja.getProduto().getId() == null) {
            throw new RegraNegocioException("O registro de estoque deve estar obrigatoriamente vinculado a um Produto.");
        }
    }
}