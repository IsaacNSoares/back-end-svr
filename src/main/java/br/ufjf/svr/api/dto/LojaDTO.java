package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Loja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LojaDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private Long idEndereco;

    public static LojaDTO create(Loja loja) {
        ModelMapper modelMapper = new ModelMapper();
        LojaDTO dto = modelMapper.map(loja, LojaDTO.class);
        return dto;
    }
}
