package com.credito.webapp.service.mapper;

import com.credito.webapp.domain.*;
import com.credito.webapp.service.dto.MovimientoCrediticioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MovimientoCrediticio} and its DTO {@link MovimientoCrediticioDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { CuentaMapper.class })
public interface MovimientoCrediticioMapper extends EntityMapper<MovimientoCrediticioDTO, MovimientoCrediticio> {
    @Mapping(source = "cuenta.id", target = "cuentaId")
    @Mapping(source = "cuenta.numeroCuenta", target = "cuenta")
    MovimientoCrediticioDTO toDto(MovimientoCrediticio movimientoCrediticio);

    @Mapping(source = "cuentaId", target = "cuenta")
    MovimientoCrediticio toEntity(MovimientoCrediticioDTO movimientoCrediticioDTO);

    default MovimientoCrediticio fromId(Long id) {
        if (id == null) {
            return null;
        }
        MovimientoCrediticio movimientoCrediticio = new MovimientoCrediticio();
        movimientoCrediticio.id = id;
        return movimientoCrediticio;
    }
}
