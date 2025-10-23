package com.credito.webapp.service.mapper;

import com.credito.webapp.domain.*;
import com.credito.webapp.service.dto.PlanDeCreditoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanDeCredito} and its DTO {@link PlanDeCreditoDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface PlanDeCreditoMapper extends EntityMapper<PlanDeCreditoDTO, PlanDeCredito> {
    @Mapping(target = "cuentas", ignore = true)
    PlanDeCredito toEntity(PlanDeCreditoDTO planDeCreditoDTO);

    default PlanDeCredito fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlanDeCredito planDeCredito = new PlanDeCredito();
        planDeCredito.id = id;
        return planDeCredito;
    }
}
