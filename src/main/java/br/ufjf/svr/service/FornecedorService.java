package br.ufjf.svr.service;

import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Fornecedor;
import br.ufjf.svr.model.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    public List<Fornecedor> getFornecedores() {
        return repository.findAll();
    }

    public Optional<Fornecedor> getFornecedorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        validar(fornecedor);
        return repository.save(fornecedor);
    }

    @Transactional
    public void excluir(Fornecedor fornecedor) {
        Objects.requireNonNull(fornecedor.getId());
        repository.delete(fornecedor);
    }

    public void validar(Fornecedor fornecedor) {
        if (fornecedor.getRazaoSocial() == null || fornecedor.getRazaoSocial().trim().equals("")) {
            throw new RegraNegocioException("Razão social inválida");
        }
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().equals("")) {
            throw new RegraNegocioException("CNPJ inválido");
        }
        if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().equals("")) {
            throw new RegraNegocioException("Email inválido");
        }
        if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }
    }
}