package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Colaborador;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ColaboradorDTO {

    private long id;
    private String nome;
    private String documento;
    private String telefone;
    private String email;
    private String dataNascimento;
    private String senha;
    private String codigo;
    private Boolean isAdmin;
    private long idLoja;

    public static ColaboradorDTO create(Colaborador colaborador) {
        ModelMapper modelMapper = new ModelMapper();
        ColaboradorDTO dto = modelMapper.map(colaborador, ColaboradorDTO.class);
        return dto;
    }

}
