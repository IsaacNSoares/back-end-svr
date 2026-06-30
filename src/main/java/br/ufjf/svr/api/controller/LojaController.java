package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.LojaDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Loja;
import br.ufjf.svr.service.LojaService;
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
@RequestMapping("/api/v1/lojas")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Lojas", description = "Operações relacionadas a lojas. Requer permissão de ADMIN para todos os endpoints.")
public class LojaController {

    private final LojaService service;

    @GetMapping()
    @Operation(summary = "Lista todas as lojas")
    public ResponseEntity get() {
        List<Loja> lojas = service.getLojas();
        return ResponseEntity.ok(lojas.stream().map(LojaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma loja pelo ID")
    public ResponseEntity get(@Parameter(description = "ID da loja a ser buscada") @PathVariable("id") Long id) {
        Optional<Loja> loja = service.getLojaById(id);
        if (!loja.isPresent()) {
            return new ResponseEntity("Loja não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(loja.map(LojaDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria uma nova loja")
    public ResponseEntity post(@RequestBody LojaDTO dto) {
        try {
            Loja loja = converter(dto);
            loja = service.salvar(loja);
            return new ResponseEntity(loja, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma loja existente")
    public ResponseEntity atualizar(@Parameter(description = "ID da loja a ser atualizada") @PathVariable("id") Long id, @RequestBody LojaDTO dto) {
        if (!service.getLojaById(id).isPresent()) {
            return new ResponseEntity("Loja não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Loja loja = converter(dto);
            loja.setId(id);
            service.salvar(loja);
            return ResponseEntity.ok(loja);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma loja")
    public ResponseEntity excluir(@Parameter(description = "ID da loja a ser excluída") @PathVariable("id") Long id) {
        Optional<Loja> loja = service.getLojaById(id);
        if (!loja.isPresent()) {
            return new ResponseEntity("Loja não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(loja.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Loja converter(LojaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Loja.class);
    }
}