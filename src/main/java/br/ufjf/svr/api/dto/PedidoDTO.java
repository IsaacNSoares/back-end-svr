package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PedidoDTO {
    private Long id;
    private String dataPedido;
    private String status;
    private Float valorTotal;
    private Long idComprador;
    private Long idVendedor;
    private Long idEndEntrega;
    private Long idPagamento;

    public static PedidoDTO create(Pedido pedido) {
        ModelMapper modelMapper = new ModelMapper();
        PedidoDTO dto = modelMapper.map(pedido, PedidoDTO.class);
        return dto;
    }
}
