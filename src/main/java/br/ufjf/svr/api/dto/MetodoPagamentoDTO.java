package br.ufjf.svr.api.dto;

import br.ufjf.svr.model.entity.MetodoPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MetodoPagamentoDTO {
    private long id;
    private String nome;
    private Boolean ativo;

    public static MetodoPagamentoDTO create(MetodoPagamento metodoPagamento) {
        ModelMapper modelMapper = new ModelMapper();
        MetodoPagamentoDTO dto = modelMapper.map(metodoPagamento, MetodoPagamentoDTO.class);
        return dto;
    }
}
