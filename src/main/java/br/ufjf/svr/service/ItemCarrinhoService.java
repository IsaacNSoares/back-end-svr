package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.ItemCarrinho;
import br.ufjf.svr.model.repository.ItemCarrinhoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemCarrinhoService {

    private final ItemCarrinhoRepository repository;

    public ItemCarrinhoService(ItemCarrinhoRepository repository) {
        this.repository = repository;
    }

    public List<ItemCarrinho> getItensCarrinho() {
        return repository.findAll();
    }

    public Optional<ItemCarrinho> getItemCarrinhoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ItemCarrinho salvar(ItemCarrinho itemCarrinho) {
        validar(itemCarrinho);
        return repository.save(itemCarrinho);
    }

    @Transactional
    public void excluir(ItemCarrinho itemCarrinho) {
        Objects.requireNonNull(itemCarrinho.getId());
        repository.delete(itemCarrinho);
    }

    public void validar(ItemCarrinho itemCarrinho) {
        if (itemCarrinho.getQuantidade() == null || itemCarrinho.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }
        if (itemCarrinho.getProduto() == null || itemCarrinho.getProduto().getId() == null) {
            throw new RegraNegocioException("Produto inválido");
        }
        if (itemCarrinho.getLojaOrigem() == null || itemCarrinho.getLojaOrigem().getId() == null) {
            throw new RegraNegocioException("Loja de origem inválida");
        }
        if (itemCarrinho.getCarrinho() == null || itemCarrinho.getCarrinho().getId() == null) {
            throw new RegraNegocioException("Carrinho inválido");
        }
    }
}