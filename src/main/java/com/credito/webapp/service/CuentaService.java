package com.credito.webapp.service;

import com.credito.webapp.domain.Cuenta;
import com.credito.webapp.service.dto.CuentaDTO;
import com.credito.webapp.service.mapper.CuentaMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class CuentaService {

    private final Logger log = LoggerFactory.getLogger(CuentaService.class);

    @Inject
    CuentaMapper cuentaMapper;

    @Transactional
    public CuentaDTO persistOrUpdate(CuentaDTO cuentaDTO) {
        log.debug("Request to save Cuenta : {}", cuentaDTO);
        var cuenta = cuentaMapper.toEntity(cuentaDTO);
        cuenta = Cuenta.persistOrUpdate(cuenta);
        return cuentaMapper.toDto(cuenta);
    }

    /**
     * Delete the Cuenta by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Cuenta : {}", id);
        Cuenta.findByIdOptional(id).ifPresent(cuenta -> {
            cuenta.delete();
        });
    }

    /**
     * Get one cuenta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<CuentaDTO> findOne(Long id) {
        log.debug("Request to get Cuenta : {}", id);
        return Cuenta.findByIdOptional(id).map(cuenta -> cuentaMapper.toDto((Cuenta) cuenta));
    }

    /**
     * Get all the cuentas.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<CuentaDTO> findAll(Page page) {
        log.debug("Request to get all Cuentas");
        return new Paged<>(Cuenta.findAll().page(page)).map(cuenta -> cuentaMapper.toDto((Cuenta) cuenta));
    }
}
