package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.CarrinhoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Carrinho;
import br.ufjf.svr.service.CarrinhoService;
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
@RequestMapping("/api/v1/carrinhos")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Carrinhos", description = "Operações relacionadas a carrinhos de compra. Requer autenticação (usuário logado) para todos os endpoints.")
public class CarrinhoController {

    private final CarrinhoService service;

    @GetMapping()
    @Operation(summary = "Lista todos os carrinhos")
    public ResponseEntity get() {
        List<Carrinho> carrinhos = service.getCarrinhos();
        return ResponseEntity.ok(carrinhos.stream().map(CarrinhoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um carrinho pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do carrinho a ser buscado") @PathVariable("id") Long id) {
        Optional<Carrinho> carrinho = service.getCarrinhoById(id);
        if (!carrinho.isPresent()) {
            return new ResponseEntity("Carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(carrinho.map(CarrinhoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo carrinho")
    public ResponseEntity post(@RequestBody CarrinhoDTO dto) {
        try {
            Carrinho carrinho = converter(dto);
            carrinho = service.salvar(carrinho);
            return new ResponseEntity(carrinho, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um carrinho existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do carrinho a ser atualizado") @PathVariable("id") Long id, @RequestBody CarrinhoDTO dto) {
        if (!service.getCarrinhoById(id).isPresent()) {
            return new ResponseEntity("Carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Carrinho carrinho = converter(dto);
            carrinho.setId(id);
            service.salvar(carrinho);
            return ResponseEntity.ok(carrinho);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um carrinho")
    public ResponseEntity excluir(@Parameter(description = "ID do carrinho a ser excluído") @PathVariable("id") Long id) {
        Optional<Carrinho> carrinho = service.getCarrinhoById(id);
        if (!carrinho.isPresent()) {
            return new ResponseEntity("Carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(carrinho.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Carrinho converter(CarrinhoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Carrinho.class);
    }
}