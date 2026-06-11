package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Carrinho;
import br.ufjf.svr.model.entity.ItemPedido;
import br.ufjf.svr.model.entity.Loja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemPedidoDTO {
    private Long id;
    private Float precoUnitario;
    private Integer quantidade;
    private Long idlojaOrigem;
    private Long idProduto;
    private Long idPedido;

    public static ItemPedidoDTO create(ItemPedido itemPedido) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(itemPedido, ItemPedidoDTO.class);
    }
}
