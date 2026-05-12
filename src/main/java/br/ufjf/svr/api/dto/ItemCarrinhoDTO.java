package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.ItemCarrinho;
import br.ufjf.svr.model.entity.Loja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemCarrinhoDTO {
    private long id;
    private Integer quantidade;
    private Loja lojaOrigem;
    private long idProduto;
    private long idCarrinho;

    public static ItemCarrinhoDTO create(ItemCarrinho itemCarrinho) {
        ModelMapper modelMapper = new ModelMapper();
        ItemCarrinhoDTO dto = modelMapper.map(itemCarrinho, ItemCarrinhoDTO.class);
        return dto;
    }
}
