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

    private Long id;
    private String nome;
    private Long idProduto;

    public static CorProdutoDTO create(CorProduto corProduto) {
        ModelMapper modelMapper = new ModelMapper();
        CorProdutoDTO dto = modelMapper.map(corProduto, CorProdutoDTO.class);
        return dto;
    }

}