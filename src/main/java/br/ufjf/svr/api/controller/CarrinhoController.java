package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.CarrinhoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Carrinho;
import br.ufjf.svr.service.CarrinhoService;
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
public class CarrinhoController {

    private final CarrinhoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Carrinho> carrinhos = service.getCarrinhos();
        return ResponseEntity.ok(carrinhos.stream().map(CarrinhoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Carrinho> carrinho = service.getCarrinhoById(id);
        if (!carrinho.isPresent()) {
            return new ResponseEntity("Carrinho não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(carrinho.map(CarrinhoDTO::create));
    }

    @PostMapping()
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
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CarrinhoDTO dto) {
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
    public ResponseEntity excluir(@PathVariable("id") Long id) {
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