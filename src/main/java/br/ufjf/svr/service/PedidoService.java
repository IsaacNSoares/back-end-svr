package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Pedido;
import br.ufjf.svr.model.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> getPedidos() {
        return repository.findAll();
    }

    public Optional<Pedido> getPedidoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        validar(pedido);
        return repository.save(pedido);
    }

    @Transactional
    public void excluir(Pedido pedido) {
        Objects.requireNonNull(pedido.getId(), "O ID do pedido não pode ser nulo para exclusão.");
        repository.delete(pedido);
    }

    public void validar(Pedido pedido) {
        if (pedido.getComprador() == null || pedido.getComprador().getId() == null) {
            throw new RegraNegocioException("O pedido deve estar obrigatoriamente vinculado a um cliente (comprador).");
        }

        if (pedido.getDataPedido() == null || pedido.getDataPedido().trim().isEmpty()) {
            throw new RegraNegocioException("A data do pedido é obrigatória.");
        }

        if (pedido.getStatus() == null || pedido.getStatus().trim().isEmpty()) {
            throw new RegraNegocioException("O status do pedido é obrigatório (ex: Pendente, Pago, Cancelado).");
        }

        if (pedido.getValorTotal() == null || pedido.getValorTotal() <= 0) {
            throw new RegraNegocioException("O valor total do pedido é inválido.");
        }

        if (pedido.getPagamento() == null || pedido.getPagamento().getId() == null) {
            throw new RegraNegocioException("O método de pagamento deve ser selecionado para finalizar o pedido.");
        }
    }
}