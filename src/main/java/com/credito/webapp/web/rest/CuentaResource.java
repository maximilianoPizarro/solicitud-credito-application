package com.credito.webapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.credito.webapp.service.CuentaService;
import com.credito.webapp.service.Paged;
import com.credito.webapp.service.dto.CuentaDTO;
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
 * REST controller for managing {@link com.credito.webapp.domain.Cuenta}.
 */
@Path("/api/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CuentaResource {

    private final Logger log = LoggerFactory.getLogger(CuentaResource.class);

    private static final String ENTITY_NAME = "cuenta";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    CuentaService cuentaService;

    /**
     * {@code POST  /cuentas} : Create a new cuenta.
     *
     * @param cuentaDTO the cuentaDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new cuentaDTO, or with status {@code 400 (Bad Request)} if the cuenta has already an ID.
     */
    @POST
    public Response createCuenta(@Valid CuentaDTO cuentaDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Cuenta : {}", cuentaDTO);
        if (cuentaDTO.id != null) {
            throw new BadRequestAlertException("A new cuenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = cuentaService.persistOrUpdate(cuentaDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /cuentas} : Updates an existing cuenta.
     *
     * @param cuentaDTO the cuentaDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated cuentaDTO,
     * or with status {@code 400 (Bad Request)} if the cuentaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuentaDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateCuenta(@Valid CuentaDTO cuentaDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Cuenta : {}", cuentaDTO);
        if (cuentaDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = cuentaService.persistOrUpdate(cuentaDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuentaDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /cuentas/:id} : delete the "id" cuenta.
     *
     * @param id the id of the cuentaDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCuenta(@PathParam("id") Long id) {
        log.debug("REST request to delete Cuenta : {}", id);
        cuentaService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /cuentas} : get all the cuentas.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of cuentas in body.
     */
    @GET
    public Response getAllCuentas(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Cuentas");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<CuentaDTO> result = cuentaService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /cuentas/:id} : get the "id" cuenta.
     *
     * @param id the id of the cuentaDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the cuentaDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getCuenta(@PathParam("id") Long id) {
        log.debug("REST request to get Cuenta : {}", id);
        Optional<CuentaDTO> cuentaDTO = cuentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuentaDTO);
    }
}
