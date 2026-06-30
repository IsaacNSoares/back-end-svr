package br.ufjf.svr.api.controller;

import br.ufjf.svr.api.dto.EnderecoDTO;
import br.ufjf.svr.exception.RegraNegocioException;
import br.ufjf.svr.model.entity.Endereco;
import br.ufjf.svr.service.EnderecoService;
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
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Endereços", description = "Operações relacionadas a endereços. Requer autenticação (usuário logado) para todos os endpoints.")
public class EnderecoController {

    private final EnderecoService service;

    @GetMapping()
    @Operation(summary = "Lista todos os endereços")
    public ResponseEntity get() {
        List<Endereco> enderecos = service.getEnderecos();
        return ResponseEntity.ok(enderecos.stream().map(EnderecoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um endereço pelo ID")
    public ResponseEntity get(@Parameter(description = "ID do endereço a ser buscado") @PathVariable("id") Long id) {
        Optional<Endereco> endereco = service.getEnderecoById(id);
        if (!endereco.isPresent()) {
            return new ResponseEntity("Endereço não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(endereco.map(EnderecoDTO::create));
    }

    @PostMapping()
    @Operation(summary = "Cria um novo endereço")
    public ResponseEntity post(@RequestBody EnderecoDTO dto) {
        try {
            Endereco endereco = converter(dto);
            endereco = service.salvar(endereco);
            return new ResponseEntity(endereco, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um endereço existente")
    public ResponseEntity atualizar(@Parameter(description = "ID do endereço a ser atualizado") @PathVariable("id") Long id, @RequestBody EnderecoDTO dto) {
        if (!service.getEnderecoById(id).isPresent()) {
            return new ResponseEntity("Endereço não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Endereco endereco = converter(dto);
            endereco.setId(id);
            service.salvar(endereco);
            return ResponseEntity.ok(endereco);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um endereço")
    public ResponseEntity excluir(@Parameter(description = "ID do endereço a ser excluído") @PathVariable("id") Long id) {
        Optional<Endereco> endereco = service.getEnderecoById(id);
        if (!endereco.isPresent()) {
            return new ResponseEntity("Endereço não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(endereco.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Endereco converter(EnderecoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Endereco.class);
    }
}