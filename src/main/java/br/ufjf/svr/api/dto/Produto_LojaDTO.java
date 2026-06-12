package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Produto_Loja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Produto_LojaDTO {
    private long id;
    private Integer quantidade;
    private long idLoja;
    private long idProduto;

    public static Produto_LojaDTO create(Produto_Loja produto_loja) {
        ModelMapper modelMapper = new ModelMapper();
        Produto_LojaDTO dto = modelMapper.map(produto_loja, Produto_LojaDTO.class);
        return dto;
    }
}
