package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.ItemPedidoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.ItemPedido;
import br.ufjf.svr.service.ItemPedidoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/itens-pedido")
@RequiredArgsConstructor
@CrossOrigin
public class ItemPedidoController {

    private final ItemPedidoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<ItemPedido> itensPedido = service.getItensPedido();
        return ResponseEntity.ok(itensPedido.stream().map(ItemPedidoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ItemPedido> itemPedido = service.getItemPedidoById(id);
        if (!itemPedido.isPresent()) {
            return new ResponseEntity("Item do pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemPedido.map(ItemPedidoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ItemPedidoDTO dto) {
        try {
            ItemPedido itemPedido = converter(dto);
            itemPedido = service.salvar(itemPedido);
            return new ResponseEntity(itemPedido, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ItemPedidoDTO dto) {
        if (!service.getItemPedidoById(id).isPresent()) {
            return new ResponseEntity("Item do pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ItemPedido itemPedido = converter(dto);
            itemPedido.setId(id);
            service.salvar(itemPedido);
            return ResponseEntity.ok(itemPedido);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ItemPedido> itemPedido = service.getItemPedidoById(id);
        if (!itemPedido.isPresent()) {
            return new ResponseEntity("Item do pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(itemPedido.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ItemPedido converter(ItemPedidoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, ItemPedido.class);
    }
}