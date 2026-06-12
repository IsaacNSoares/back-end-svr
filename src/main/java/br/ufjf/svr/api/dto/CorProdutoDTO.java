package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.CorProduto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CorProdutoDTO {

    private long id;
    private String nome;
    private long idProduto;

    public static CorProdutoDTO create(CorProduto corProduto) {
        ModelMapper modelMapper = new ModelMapper();
        CorProdutoDTO dto = modelMapper.map(corProduto, CorProdutoDTO.class);
        return dto;
    }

}