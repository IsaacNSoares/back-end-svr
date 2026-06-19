package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.ItemCarrinho;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemCarrinhoDTO {
    private Long id;
    private Integer quantidade;
    private Long idlojaOrigem;
    private Long idProduto;
    private Long idCarrinho;

    public static ItemCarrinhoDTO create(ItemCarrinho itemCarrinho) {
        ModelMapper modelMapper = new ModelMapper();
        ItemCarrinhoDTO dto = modelMapper.map(itemCarrinho, ItemCarrinhoDTO.class);
        return dto;
    }
}
