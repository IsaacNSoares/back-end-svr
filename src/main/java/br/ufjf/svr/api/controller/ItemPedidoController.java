package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.ItemPedidoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.ItemPedido;
import br.ufjf.svr.service.ItemPedidoService;
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
@RequestMapping("/api/v1/itens-pedido")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Itens do Pedido", description = "Operações relacionadas a itens de pedido. Requer autenticação (usuário logado) para todos os endpoints.")
public class ItemPedidoController {

    private final ItemPedidoService service;

    @GetMapping()
    @Operation(summary = "Lista todos os itens de pedido")
    public ResponseEntity get() {
        List<ItemPedido> itensPedido = service.getItensPedido();
        return ResponseEntity.ok(itensPedido.stream().map(ItemPedidoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um item de pedido pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do item de pedido a ser buscado") @PathVariable("id") Long id) {
        Optional<ItemPedido> itemPedido = service.getItemPedidoById(id);
        if (!itemPedido.isPresent()) {
            return new ResponseEntity("Item do pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemPedido.map(ItemPedidoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo item de pedido")
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
    @Operation(summary = "Atualiza um item de pedido existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do item de pedido a ser atualizado") @PathVariable("id") Long id, @RequestBody ItemPedidoDTO dto) {
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
    @Operation(summary = "Exclui um item de pedido")
    public ResponseEntity excluir(@Parameter(description = "ID do item de pedido a ser excluído") @PathVariable("id") Long id) {
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