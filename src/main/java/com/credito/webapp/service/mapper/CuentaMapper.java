package com.credito.webapp.service.mapper;

import com.credito.webapp.domain.*;
import com.credito.webapp.service.dto.CuentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cuenta} and its DTO {@link CuentaDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { ClienteMapper.class, PlanDeCreditoMapper.class })
public interface CuentaMapper extends EntityMapper<CuentaDTO, Cuenta> {
    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "cliente.numeroDocumento", target = "cliente")
    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.nombre", target = "plan")
    CuentaDTO toDto(Cuenta cuenta);

    @Mapping(target = "movimientos", ignore = true)
    @Mapping(source = "clienteId", target = "cliente")
    @Mapping(source = "planId", target = "plan")
    Cuenta toEntity(CuentaDTO cuentaDTO);

    default Cuenta fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cuenta cuenta = new Cuenta();
        cuenta.id = id;
        return cuenta;
    }
}
