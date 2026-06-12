package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.Carrinho;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CarrinhoDTO {

    private long id;
    private long idCliente;
    private long idColaborador;

    public static CarrinhoDTO create(Carrinho carrinho) {
        ModelMapper modelMapper = new ModelMapper();
        CarrinhoDTO dto = modelMapper.map(carrinho, CarrinhoDTO.class);
        return dto;
    }

}