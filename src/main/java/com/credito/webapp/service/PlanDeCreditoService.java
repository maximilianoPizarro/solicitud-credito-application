package com.credito.webapp.service;

import com.credito.webapp.domain.PlanDeCredito;
import com.credito.webapp.service.dto.PlanDeCreditoDTO;
import com.credito.webapp.service.mapper.PlanDeCreditoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class PlanDeCreditoService {

    private final Logger log = LoggerFactory.getLogger(PlanDeCreditoService.class);

    @Inject
    PlanDeCreditoMapper planDeCreditoMapper;

    @Transactional
    public PlanDeCreditoDTO persistOrUpdate(PlanDeCreditoDTO planDeCreditoDTO) {
        log.debug("Request to save PlanDeCredito : {}", planDeCreditoDTO);
        var planDeCredito = planDeCreditoMapper.toEntity(planDeCreditoDTO);
        planDeCredito = PlanDeCredito.persistOrUpdate(planDeCredito);
        return planDeCreditoMapper.toDto(planDeCredito);
    }

    /**
     * Delete the PlanDeCredito by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete PlanDeCredito : {}", id);
        PlanDeCredito.findByIdOptional(id).ifPresent(planDeCredito -> {
            planDeCredito.delete();
        });
    }

    /**
     * Get one planDeCredito by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PlanDeCreditoDTO> findOne(Long id) {
        log.debug("Request to get PlanDeCredito : {}", id);
        return PlanDeCredito.findByIdOptional(id).map(planDeCredito -> planDeCreditoMapper.toDto((PlanDeCredito) planDeCredito));
    }

    /**
     * Get all the planDeCreditos.
     * @return the list of entities.
     */
    public List<PlanDeCreditoDTO> findAll() {
        log.debug("Request to get all PlanDeCreditos");
        List<PlanDeCredito> planDeCreditos = PlanDeCredito.findAll().list();
        return planDeCreditoMapper.toDto(planDeCreditos);
    }
}
