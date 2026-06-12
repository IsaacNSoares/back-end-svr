package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.Produto_LojaDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Produto_Loja;
import br.ufjf.svr.service.Produto_LojaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/produtos-loja")
@RequiredArgsConstructor
@CrossOrigin
public class Produto_LojaController {

    private final Produto_LojaService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Produto_Loja> estoques = service.getEstoques();
        return ResponseEntity.ok(estoques.stream().map(Produto_LojaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Produto_Loja> estoque = service.getEstoqueById(id);
        if (!estoque.isPresent()) {
            return new ResponseEntity("Registro de estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estoque.map(Produto_LojaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody Produto_LojaDTO dto) {
        try {
            Produto_Loja produtoLoja = converter(dto);
            produtoLoja = service.salvar(produtoLoja);
            return new ResponseEntity(produtoLoja, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody Produto_LojaDTO dto) {
        if (!service.getEstoqueById(id).isPresent()) {
            return new ResponseEntity("Registro de estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Produto_Loja produtoLoja = converter(dto);
            produtoLoja.setId(id);
            service.salvar(produtoLoja);
            return ResponseEntity.ok(produtoLoja);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Produto_Loja> estoque = service.getEstoqueById(id);
        if (!estoque.isPresent()) {
            return new ResponseEntity("Registro de estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(estoque.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Produto_Loja converter(Produto_LojaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Produto_Loja.class);
    }
}