package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.TamanhoProdutoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.TamanhoProduto;
import br.ufjf.svr.service.TamanhoProdutoService;
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
public class TamanhoProdutoController {

    private final TamanhoProdutoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<TamanhoProduto> tamanhosProduto = service.getTamanhos();
        return ResponseEntity.ok(tamanhosProduto.stream().map(TamanhoProdutoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<TamanhoProduto> tamanhoProduto = service.getTamanhoById(id);
        if (!tamanhoProduto.isPresent()) {
            return new ResponseEntity("Tamanho de produto não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tamanhoProduto.map(TamanhoProdutoDTO::create));
    }

    @PostMapping()
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
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TamanhoProdutoDTO dto) {
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
    public ResponseEntity excluir(@PathVariable("id") Long id) {
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