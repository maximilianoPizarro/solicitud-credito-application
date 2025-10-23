package com.credito.webapp.service;

import com.credito.webapp.domain.MovimientoCrediticio;
import com.credito.webapp.service.dto.MovimientoCrediticioDTO;
import com.credito.webapp.service.mapper.MovimientoCrediticioMapper;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class MovimientoCrediticioService {

    private final Logger log = LoggerFactory.getLogger(MovimientoCrediticioService.class);

    @Inject
    MovimientoCrediticioMapper movimientoCrediticioMapper;

    @Transactional
    public MovimientoCrediticioDTO persistOrUpdate(MovimientoCrediticioDTO movimientoCrediticioDTO) {
        log.debug("Request to save MovimientoCrediticio : {}", movimientoCrediticioDTO);
        var movimientoCrediticio = movimientoCrediticioMapper.toEntity(movimientoCrediticioDTO);
        movimientoCrediticio = MovimientoCrediticio.persistOrUpdate(movimientoCrediticio);
        return movimientoCrediticioMapper.toDto(movimientoCrediticio);
    }

    /**
     * Delete the MovimientoCrediticio by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete MovimientoCrediticio : {}", id);
        MovimientoCrediticio.findByIdOptional(id).ifPresent(movimientoCrediticio -> {
            movimientoCrediticio.delete();
        });
    }

    /**
     * Get one movimientoCrediticio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MovimientoCrediticioDTO> findOne(Long id) {
        log.debug("Request to get MovimientoCrediticio : {}", id);
        return MovimientoCrediticio.findByIdOptional(id).map(movimientoCrediticio ->
            movimientoCrediticioMapper.toDto((MovimientoCrediticio) movimientoCrediticio)
        );
    }

    /**
     * Get all the movimientoCrediticios.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<MovimientoCrediticioDTO> findAll(Page page) {
        log.debug("Request to get all MovimientoCrediticios");
        return new Paged<>(MovimientoCrediticio.findAll().page(page)).map(movimientoCrediticio ->
            movimientoCrediticioMapper.toDto((MovimientoCrediticio) movimientoCrediticio)
        );
    }
}
