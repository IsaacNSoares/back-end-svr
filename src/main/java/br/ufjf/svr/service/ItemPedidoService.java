package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.ItemPedido;
import br.ufjf.svr.model.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository repository;

    public ItemPedidoService(ItemPedidoRepository repository) {
        this.repository = repository;
    }

    public List<ItemPedido> getItensPedido() {
        return repository.findAll();
    }

    public Optional<ItemPedido> getItemPedidoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ItemPedido salvar(ItemPedido itemPedido) {
        validar(itemPedido);
        return repository.save(itemPedido);
    }

    @Transactional
    public void excluir(ItemPedido itemPedido) {
        Objects.requireNonNull(itemPedido.getId());
        repository.delete(itemPedido);
    }

    public void validar(ItemPedido itemPedido) {
        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() <= 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }
        if (itemPedido.getProduto() == null || itemPedido.getProduto().getId() == null) {
            throw new RegraNegocioException("Produto inválido");
        }
        if (itemPedido.getLojaOrigem() == null || itemPedido.getLojaOrigem().getId() == null) {
            throw new RegraNegocioException("Loja de origem inválida");
        }
        if (itemPedido.getPedido() == null || itemPedido.getPedido().getId() == null) {
            throw new RegraNegocioException("Pedido inválido");
        }
        // Validação do preço unitário congelado usando a tipagem Long (em centavos)
        if (itemPedido.getPrecoUnitario() == null || itemPedido.getPrecoUnitario() <= 0) {
            throw new RegraNegocioException("Preço unitário inválido");
        }
    }
}