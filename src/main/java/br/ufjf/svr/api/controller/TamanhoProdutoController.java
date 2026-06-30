package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.TamanhoProdutoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.TamanhoProduto;
import br.ufjf.svr.service.TamanhoProdutoService;
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
@RequestMapping("/api/v1/tamanhos-produto")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Tamanhos de Produto", description = "Operações relacionadas a tamanhos de produto. Acesso público (não requer autenticação) para todos os endpoints.")
public class TamanhoProdutoController {

    private final TamanhoProdutoService service;

    @GetMapping()
    @Operation(summary = "Lista todos os tamanhos de produto")
    public ResponseEntity get() {
        List<TamanhoProduto> tamanhosProduto = service.getTamanhos();
        return ResponseEntity.ok(tamanhosProduto.stream().map(TamanhoProdutoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um tamanho de produto pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do tamanho de produto a ser buscado") @PathVariable("id") Long id) {
        Optional<TamanhoProduto> tamanhoProduto = service.getTamanhoById(id);
        if (!tamanhoProduto.isPresent()) {
            return new ResponseEntity("Tamanho de produto não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tamanhoProduto.map(TamanhoProdutoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo tamanho de produto", description = "O corpo da requisição deve conter os dados do tamanho, incluindo o produtoId (ID do produto ao qual esse tamanho está vinculado).")
    public ResponseEntity post(@RequestBody TamanhoProdutoDTO dto) {
        try {
            TamanhoProduto tamanhoProduto = converter(dto);
            tamanhoProduto = service.salvar(tamanhoProduto);
            return new ResponseEntity(tamanhoProduto, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um tamanho de produto existente", description = "O corpo da requisição deve conter os dados do tamanho, incluindo o produtoId (ID do produto ao qual esse tamanho está vinculado).")
    public ResponseEntity atualizar(@Parameter(description = "ID do tamanho de produto a ser atualizado") @PathVariable("id") Long id, @RequestBody TamanhoProdutoDTO dto) {
        if (!service.getTamanhoById(id).isPresent()) {
            return new ResponseEntity("Tamanho de produto não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            TamanhoProduto tamanhoProduto = converter(dto);
            tamanhoProduto.setId(id);
            service.salvar(tamanhoProduto);
            return ResponseEntity.ok(tamanhoProduto);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um tamanho de produto")
    public ResponseEntity excluir(@Parameter(description = "ID do tamanho de produto a ser excluído") @PathVariable("id") Long id) {
        Optional<TamanhoProduto> tamanhoProduto = service.getTamanhoById(id);
        if (!tamanhoProduto.isPresent()) {
            return new ResponseEntity("Tamanho de produto não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tamanhoProduto.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public TamanhoProduto converter(TamanhoProdutoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, TamanhoProduto.class);
    }
}