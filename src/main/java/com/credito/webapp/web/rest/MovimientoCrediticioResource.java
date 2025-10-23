package com.credito.webapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.credito.webapp.service.MovimientoCrediticioService;
import com.credito.webapp.service.Paged;
import com.credito.webapp.service.dto.MovimientoCrediticioDTO;
import com.credito.webapp.web.rest.errors.BadRequestAlertException;
import com.credito.webapp.web.rest.vm.PageRequestVM;
import com.credito.webapp.web.rest.vm.SortRequestVM;
import com.credito.webapp.web.util.HeaderUtil;
import com.credito.webapp.web.util.PaginationUtil;
import com.credito.webapp.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.credito.webapp.domain.MovimientoCrediticio}.
 */
@Path("/api/movimiento-crediticios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MovimientoCrediticioResource {

    private final Logger log = LoggerFactory.getLogger(MovimientoCrediticioResource.class);

    private static final String ENTITY_NAME = "movimientoCrediticio";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    MovimientoCrediticioService movimientoCrediticioService;

    /**
     * {@code POST  /movimiento-crediticios} : Create a new movimientoCrediticio.
     *
     * @param movimientoCrediticioDTO the movimientoCrediticioDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new movimientoCrediticioDTO, or with status {@code 400 (Bad Request)} if the movimientoCrediticio has already an ID.
     */
    @POST
    public Response createMovimientoCrediticio(@Valid MovimientoCrediticioDTO movimientoCrediticioDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save MovimientoCrediticio : {}", movimientoCrediticioDTO);
        if (movimientoCrediticioDTO.id != null) {
            throw new BadRequestAlertException("A new movimientoCrediticio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = movimientoCrediticioService.persistOrUpdate(movimientoCrediticioDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /movimiento-crediticios} : Updates an existing movimientoCrediticio.
     *
     * @param movimientoCrediticioDTO the movimientoCrediticioDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated movimientoCrediticioDTO,
     * or with status {@code 400 (Bad Request)} if the movimientoCrediticioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movimientoCrediticioDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateMovimientoCrediticio(@Valid MovimientoCrediticioDTO movimientoCrediticioDTO, @PathParam("id") Long id) {
        log.debug("REST request to update MovimientoCrediticio : {}", movimientoCrediticioDTO);
        if (movimientoCrediticioDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = movimientoCrediticioService.persistOrUpdate(movimientoCrediticioDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movimientoCrediticioDTO.id.toString()).forEach(
            response::header
        );
        return response.build();
    }

    /**
     * {@code DELETE  /movimiento-crediticios/:id} : delete the "id" movimientoCrediticio.
     *
     * @param id the id of the movimientoCrediticioDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteMovimientoCrediticio(@PathParam("id") Long id) {
        log.debug("REST request to delete MovimientoCrediticio : {}", id);
        movimientoCrediticioService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /movimiento-crediticios} : get all the movimientoCrediticios.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of movimientoCrediticios in body.
     */
    @GET
    public Response getAllMovimientoCrediticios(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of MovimientoCrediticios");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<MovimientoCrediticioDTO> result = movimientoCrediticioService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /movimiento-crediticios/:id} : get the "id" movimientoCrediticio.
     *
     * @param id the id of the movimientoCrediticioDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the movimientoCrediticioDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getMovimientoCrediticio(@PathParam("id") Long id) {
        log.debug("REST request to get MovimientoCrediticio : {}", id);
        Optional<MovimientoCrediticioDTO> movimientoCrediticioDTO = movimientoCrediticioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movimientoCrediticioDTO);
    }
}
