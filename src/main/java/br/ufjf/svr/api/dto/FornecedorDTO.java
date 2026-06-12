package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Fornecedor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FornecedorDTO {
    private long id;
    private String razaoSocial;
    private String cnpj;
    private String telefone;
    private String email;

    public static FornecedorDTO create(Fornecedor fornecedor) {
        ModelMapper modelMapper = new ModelMapper();
        FornecedorDTO dto = modelMapper.map(fornecedor, FornecedorDTO.class);
        return dto;
    }
}
