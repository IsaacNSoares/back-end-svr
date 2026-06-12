package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.*;
import br.ufjf.svr.model.repository.CarrinhoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarrinhoService {
    private CarrinhoRepository repository;

    public CarrinhoService(CarrinhoRepository repository) {
        this.repository = repository;
    }

    public List<Carrinho> getCarrinhos() {
        return repository.findAll();
    }

    public Optional<Carrinho> getCarrinhoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Carrinho salvar(Carrinho carrinho) {
        validar(carrinho);
        return repository.save(carrinho);
    }

    @Transactional
    public void excluir(Carrinho carrinho) {
        Objects.requireNonNull(carrinho.getId());
        repository.delete(carrinho);
    }

    public void validar(Carrinho carrinho) {
        if (carrinho.getCliente() == null || carrinho.getCliente() == null) {
            throw new RegraNegocioException("Cliente inválido");
        }
        if (carrinho.getColaborador() == null || carrinho.getColaborador().getId() == null) {
            throw new RegraNegocioException("Colaborador inválido");
        }
    }

}