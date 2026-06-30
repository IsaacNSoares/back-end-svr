package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.PedidoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Pedido;
import br.ufjf.svr.service.PedidoService;
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
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos. Requer autenticação (usuário logado) para todos os endpoints.")
public class PedidoController {

    private final PedidoService service;

    @GetMapping()
    @Operation(summary = "Lista todos os pedidos")
    public ResponseEntity get() {
        List<Pedido> pedidos = service.getPedidos();
        return ResponseEntity.ok(pedidos.stream().map(PedidoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pedido pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do pedido a ser buscado") @PathVariable("id") Long id) {
        Optional<Pedido> pedido = service.getPedidoById(id);
        if (!pedido.isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pedido.map(PedidoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo pedido")
    public ResponseEntity post(@RequestBody PedidoDTO dto) {
        try {
            Pedido pedido = converter(dto);
            pedido = service.salvar(pedido);
            return new ResponseEntity(pedido, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um pedido existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do pedido a ser atualizado") @PathVariable("id") Long id, @RequestBody PedidoDTO dto) {
        if (!service.getPedidoById(id).isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Pedido pedido = converter(dto);
            pedido.setId(id);
            service.salvar(pedido);
            return ResponseEntity.ok(pedido);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um pedido")
    public ResponseEntity excluir(@Parameter(description = "ID do pedido a ser excluído") @PathVariable("id") Long id) {
        Optional<Pedido> pedido = service.getPedidoById(id);
        if (!pedido.isPresent()) {
            return new ResponseEntity("Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(pedido.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Pedido converter(PedidoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Pedido.class);
    }
}