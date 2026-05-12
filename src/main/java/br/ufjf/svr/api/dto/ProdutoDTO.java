package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoDTO {
    private long id;
    private String sku;
    private String nome;
    private String descricao;
    private String urlFoto;
    private Float precoVarejo;
    private Float precoAtacado;
    private long idFornecedor;

    public static ProdutoDTO create(Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        return dto;
    }
}
