package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.TamanhoProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TamanhoProdutoDTO {
    private long id;
    private String nome;
    private long idProduto;

    public static TamanhoProdutoDTO create(TamanhoProduto tamanhoProduto) {
        ModelMapper modelMapper = new ModelMapper();
        TamanhoProdutoDTO dto = modelMapper.map(tamanhoProduto, TamanhoProdutoDTO.class);
        return dto;
    }
}
