package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.ColaboradorDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Colaborador;
import br.ufjf.svr.service.ColaboradorService;
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
public class ColaboradorController {

    private final ColaboradorService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Colaborador> colaboradores = service.getColaboradores();
        return ResponseEntity.ok(colaboradores.stream().map(ColaboradorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Colaborador> colaborador = service.getColaboradorById(id);
        if (!colaborador.isPresent()) {
            return new ResponseEntity("Colaborador não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(colaborador.map(ColaboradorDTO::create));
    }

    @PostMapping()
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
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ColaboradorDTO dto) {
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
    public ResponseEntity excluir(@PathVariable("id") Long id) {
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