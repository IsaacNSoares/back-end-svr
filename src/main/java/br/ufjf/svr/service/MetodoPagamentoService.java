package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.MetodoPagamento;
import br.ufjf.svr.model.repository.MetodoPagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MetodoPagamentoService {

    private final MetodoPagamentoRepository repository;

    public MetodoPagamentoService(MetodoPagamentoRepository repository) {
        this.repository = repository;
    }

    public List<MetodoPagamento> getMetodosPagamento() {
        return repository.findAll();
    }

    public Optional<MetodoPagamento> getMetodoPagamentoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MetodoPagamento salvar(MetodoPagamento metodoPagamento) {
        validar(metodoPagamento);
        return repository.save(metodoPagamento);
    }

    @Transactional
    public void excluir(MetodoPagamento metodoPagamento) {
        Objects.requireNonNull(metodoPagamento.getId(), "O ID do método de pagamento não pode ser nulo para exclusão.");
        repository.delete(metodoPagamento);
    }

    public void validar(MetodoPagamento metodoPagamento) {
        if (metodoPagamento.getNome() == null || metodoPagamento.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("O nome do método de pagamento é obrigatório e não pode estar em branco.");
        }

        if (metodoPagamento.getAtivo() == null) {
            throw new RegraNegocioException("O status ativo/inativo do método de pagamento deve ser explicitamente informado.");
        }
    }
}