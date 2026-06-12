package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.MetodoPagamentoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.MetodoPagamento;
import br.ufjf.svr.service.MetodoPagamentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/metodos-pagamento")
@RequiredArgsConstructor
@CrossOrigin
public class MetodoPagamentoController {

    private final MetodoPagamentoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<MetodoPagamento> metodosPagamento = service.getMetodosPagamento();
        return ResponseEntity.ok(metodosPagamento.stream().map(MetodoPagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<MetodoPagamento> metodoPagamento = service.getMetodoPagamentoById(id);
        if (!metodoPagamento.isPresent()) {
            return new ResponseEntity("Método de pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(metodoPagamento.map(MetodoPagamentoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody MetodoPagamentoDTO dto) {
        try {
            MetodoPagamento metodoPagamento = converter(dto);
            metodoPagamento = service.salvar(metodoPagamento);
            return new ResponseEntity(metodoPagamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MetodoPagamentoDTO dto) {
        if (!service.getMetodoPagamentoById(id).isPresent()) {
            return new ResponseEntity("Método de pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            MetodoPagamento metodoPagamento = converter(dto);
            metodoPagamento.setId(id);
            service.salvar(metodoPagamento);
            return ResponseEntity.ok(metodoPagamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<MetodoPagamento> metodoPagamento = service.getMetodoPagamentoById(id);
        if (!metodoPagamento.isPresent()) {
            return new ResponseEntity("Método de pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(metodoPagamento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public MetodoPagamento converter(MetodoPagamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, MetodoPagamento.class);
    }
}