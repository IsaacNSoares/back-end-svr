package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Carrinho;
import br.ufjf.svr.model.entity.Loja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemPedidoDTO {
    private long id;
    private Float precoUnitario;
    private Integer quantidade;
    private Loja lojaOrigem;
    private long idProduto;
    private long idPedido;

    public static CarrinhoDTO create(Carrinho carrinho) {
        ModelMapper modelMapper = new ModelMapper();
        CarrinhoDTO dto = modelMapper.map(carrinho, CarrinhoDTO.class);
        return dto;
    }
}
