package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.CorProdutoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.CorProduto;
import br.ufjf.svr.service.CorProdutoService;
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
@RequestMapping("/api/v1/cores-produto")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Cores de Produto", description = "Operações relacionadas a cores de produto. Acesso público (não requer autenticação) para todos os endpoints.")
public class CorProdutoController {

    private final CorProdutoService service;

    @GetMapping()
    @Operation(summary = "Lista todas as cores de produto")
    public ResponseEntity get() {
        List<CorProduto> coresProduto = service.getCorProdutos();
        return ResponseEntity.ok(coresProduto.stream().map(CorProdutoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma cor de produto pelo ID")
    public ResponseEntity get(@Parameter(description = "ID da cor de produto a ser buscada") @PathVariable("id") Long id) {
        Optional<CorProduto> corProduto = service.getCorProdutoById(id);
        if (!corProduto.isPresent()) {
            return new ResponseEntity("Cor de produto não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(corProduto.map(CorProdutoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria uma nova cor de produto")
    public ResponseEntity post(@RequestBody CorProdutoDTO dto) {
        try {
            CorProduto corProduto = converter(dto);
            corProduto = service.salvar(corProduto);
            return new ResponseEntity(corProduto, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma cor de produto existente")
    public ResponseEntity atualizar(@Parameter(description = "ID da cor de produto a ser atualizada") @PathVariable("id") Long id, @RequestBody CorProdutoDTO dto) {
        if (!service.getCorProdutoById(id).isPresent()) {
            return new ResponseEntity("Cor de produto não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            CorProduto corProduto = converter(dto);
            corProduto.setId(id);
            service.salvar(corProduto);
            return ResponseEntity.ok(corProduto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma cor de produto")
    public ResponseEntity excluir(@Parameter(description = "ID da cor de produto a ser excluída") @PathVariable("id") Long id) {
        Optional<CorProduto> corProduto = service.getCorProdutoById(id);
        if (!corProduto.isPresent()) {
            return new ResponseEntity("Cor de produto não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(corProduto.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public CorProduto converter(CorProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, CorProduto.class);
    }
}