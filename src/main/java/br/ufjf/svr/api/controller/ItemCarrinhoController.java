package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.ItemCarrinhoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.ItemCarrinho;
import br.ufjf.svr.service.ItemCarrinhoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/itens-carrinho")
@RequiredArgsConstructor
@CrossOrigin
public class ItemCarrinhoController {

    private final ItemCarrinhoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<ItemCarrinho> itensCarrinho = service.getItensCarrinho();
        return ResponseEntity.ok(itensCarrinho.stream().map(ItemCarrinhoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ItemCarrinho> itemCarrinho = service.getItemCarrinhoById(id);
        if (!itemCarrinho.isPresent()) {
            return new ResponseEntity("Item do carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemCarrinho.map(ItemCarrinhoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ItemCarrinhoDTO dto) {
        try {
            ItemCarrinho itemCarrinho = converter(dto);
            itemCarrinho = service.salvar(itemCarrinho);
            return new ResponseEntity(itemCarrinho, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ItemCarrinhoDTO dto) {
        if (!service.getItemCarrinhoById(id).isPresent()) {
            return new ResponseEntity("Item do carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ItemCarrinho itemCarrinho = converter(dto);
            itemCarrinho.setId(id);
            service.salvar(itemCarrinho);
            return ResponseEntity.ok(itemCarrinho);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ItemCarrinho> itemCarrinho = service.getItemCarrinhoById(id);
        if (!itemCarrinho.isPresent()) {
            return new ResponseEntity("Item do carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(itemCarrinho.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ItemCarrinho converter(ItemCarrinhoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, ItemCarrinho.class);
    }
}