package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.FornecedorDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Fornecedor;
import br.ufjf.svr.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fornecedores")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Fornecedores", description = "Operações relacionadas a fornecedores. Requer permissão de ADMIN para todos os endpoints.")
public class FornecedorController {

    private final FornecedorService service;

    @GetMapping()
    @Operation(summary = "Lista todos os fornecedores")
    public ResponseEntity get() {
        List<Fornecedor> fornecedores = service.getFornecedores();
        return ResponseEntity.ok(fornecedores.stream().map(FornecedorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um fornecedor pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do fornecedor a ser buscado") @PathVariable("id") Long id) {
        Optional<Fornecedor> fornecedor = service.getFornecedorById(id);
        if (!fornecedor.isPresent()) {
            return new ResponseEntity("Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fornecedor.map(FornecedorDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo fornecedor")
    public ResponseEntity post(@RequestBody FornecedorDTO dto) {
        try {
            Fornecedor fornecedor = converter(dto);
            fornecedor = service.salvar(fornecedor);
            return new ResponseEntity(fornecedor, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um fornecedor existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do fornecedor a ser atualizado") @PathVariable("id") Long id, @RequestBody FornecedorDTO dto) {
        if (!service.getFornecedorById(id).isPresent()) {
            return new ResponseEntity("Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Fornecedor fornecedor = converter(dto);
            fornecedor.setId(id);
            service.salvar(fornecedor);
            return ResponseEntity.ok(fornecedor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um fornecedor")
    public ResponseEntity excluir(@Parameter(description = "ID do fornecedor a ser excluído") @PathVariable("id") Long id) {
        Optional<Fornecedor> fornecedor = service.getFornecedorById(id);
        if (!fornecedor.isPresent()) {
            return new ResponseEntity("Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(fornecedor.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Fornecedor converter(FornecedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Fornecedor.class);
    }
}