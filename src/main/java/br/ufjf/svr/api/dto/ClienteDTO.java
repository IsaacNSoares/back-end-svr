package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Cliente;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteDTO {

    private Long id;
    private String nome;
    private String documento;
    private String telefone;
    private String email;
    private String dataNascimento;
    private String senha;
    private Boolean pessoaJuridica;
    private Long idMetodo;

    public static ClienteDTO create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
        return dto;
    }

}