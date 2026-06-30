package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.ClienteDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Cliente;
import br.ufjf.svr.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Clientes", description = "Operações relacionadas a clientes. Acesso público (não requer autenticação) para todos os endpoints.")
public class ClienteController {

    private final ClienteService service;

    @GetMapping()
    @Operation(summary = "Lista todos os clientes")
    public ResponseEntity get() {
        List<Cliente> clientes = service.getClientes();
        return ResponseEntity.ok(clientes.stream().map(ClienteDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do cliente a ser buscado") @PathVariable("id") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cliente.map(ClienteDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo cliente")
    public ResponseEntity post(@RequestBody ClienteDTO dto) {
        try {
            Cliente cliente = converter(dto);
            cliente = service.salvar(cliente);
            return new ResponseEntity(cliente, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um cliente existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do cliente a ser atualizado") @PathVariable("id") Long id, @RequestBody ClienteDTO dto) {
        if (!service.getClienteById(id).isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Cliente cliente = converter(dto);
            cliente.setId(id);
            service.salvar(cliente);
            return ResponseEntity.ok(cliente);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um cliente")
    public ResponseEntity excluir(@Parameter(description = "ID do cliente a ser excluído") @PathVariable("id") Long id) {
        Optional<Cliente> cliente = service.getClienteById(id);
        if (!cliente.isPresent()) {
            return new ResponseEntity("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(cliente.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Cliente converter(ClienteDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Cliente.class);
    }
}