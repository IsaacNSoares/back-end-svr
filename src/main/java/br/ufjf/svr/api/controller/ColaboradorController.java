package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.ColaboradorDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Colaborador;
import br.ufjf.svr.service.ColaboradorService;
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
@RequestMapping("/api/v1/colaboradores")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Colaboradores", description = "Operações relacionadas a colaboradores. Requer permissão de ADMIN para todos os endpoints.")
public class ColaboradorController {

    private final ColaboradorService service;

    @GetMapping()
    @Operation(summary = "Lista todos os colaboradores")
    public ResponseEntity get() {
        List<Colaborador> colaboradores = service.getColaboradores();
        return ResponseEntity.ok(colaboradores.stream().map(ColaboradorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um colaborador pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do colaborador a ser buscado") @PathVariable("id") Long id) {
        Optional<Colaborador> colaborador = service.getColaboradorById(id);
        if (!colaborador.isPresent()) {
            return new ResponseEntity("Colaborador não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(colaborador.map(ColaboradorDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo colaborador")
    public ResponseEntity post(@RequestBody ColaboradorDTO dto) {
        try {
            Colaborador colaborador = converter(dto);
            colaborador = service.salvar(colaborador);
            return new ResponseEntity(colaborador, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um colaborador existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do colaborador a ser atualizado") @PathVariable("id") Long id, @RequestBody ColaboradorDTO dto) {
        if (!service.getColaboradorById(id).isPresent()) {
            return new ResponseEntity("Colaborador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Colaborador colaborador = converter(dto);
            colaborador.setId(id);
            service.salvar(colaborador);
            return ResponseEntity.ok(colaborador);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um colaborador")
    public ResponseEntity excluir(@Parameter(description = "ID do colaborador a ser excluído") @PathVariable("id") Long id) {
        Optional<Colaborador> colaborador = service.getColaboradorById(id);
        if (!colaborador.isPresent()) {
            return new ResponseEntity("Colaborador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(colaborador.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Colaborador converter(ColaboradorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Colaborador.class);
    }
}