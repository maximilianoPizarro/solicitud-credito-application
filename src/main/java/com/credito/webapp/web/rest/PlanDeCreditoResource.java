package com.credito.webapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.credito.webapp.service.PlanDeCreditoService;
import com.credito.webapp.service.dto.PlanDeCreditoDTO;
import com.credito.webapp.web.rest.errors.BadRequestAlertException;
import com.credito.webapp.web.util.HeaderUtil;
import com.credito.webapp.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link com.credito.webapp.domain.PlanDeCredito}.
 */
@Path("/api/plan-de-creditos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PlanDeCreditoResource {

    private final Logger log = LoggerFactory.getLogger(PlanDeCreditoResource.class);

    private static final String ENTITY_NAME = "planDeCredito";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    PlanDeCreditoService planDeCreditoService;

    /**
     * {@code POST  /plan-de-creditos} : Create a new planDeCredito.
     *
     * @param planDeCreditoDTO the planDeCreditoDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new planDeCreditoDTO, or with status {@code 400 (Bad Request)} if the planDeCredito has already an ID.
     */
    @POST
    public Response createPlanDeCredito(@Valid PlanDeCreditoDTO planDeCreditoDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save PlanDeCredito : {}", planDeCreditoDTO);
        if (planDeCreditoDTO.id != null) {
            throw new BadRequestAlertException("A new planDeCredito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = planDeCreditoService.persistOrUpdate(planDeCreditoDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /plan-de-creditos} : Updates an existing planDeCredito.
     *
     * @param planDeCreditoDTO the planDeCreditoDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated planDeCreditoDTO,
     * or with status {@code 400 (Bad Request)} if the planDeCreditoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planDeCreditoDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updatePlanDeCredito(@Valid PlanDeCreditoDTO planDeCreditoDTO, @PathParam("id") Long id) {
        log.debug("REST request to update PlanDeCredito : {}", planDeCreditoDTO);
        if (planDeCreditoDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = planDeCreditoService.persistOrUpdate(planDeCreditoDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planDeCreditoDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /plan-de-creditos/:id} : delete the "id" planDeCredito.
     *
     * @param id the id of the planDeCreditoDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deletePlanDeCredito(@PathParam("id") Long id) {
        log.debug("REST request to delete PlanDeCredito : {}", id);
        planDeCreditoService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /plan-de-creditos} : get all the planDeCreditos.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of planDeCreditos in body.
     */
    @GET
    public List<PlanDeCreditoDTO> getAllPlanDeCreditos() {
        log.debug("REST request to get all PlanDeCreditos");
        return planDeCreditoService.findAll();
    }

    /**
     * {@code GET  /plan-de-creditos/:id} : get the "id" planDeCredito.
     *
     * @param id the id of the planDeCreditoDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the planDeCreditoDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getPlanDeCredito(@PathParam("id") Long id) {
        log.debug("REST request to get PlanDeCredito : {}", id);
        Optional<PlanDeCreditoDTO> planDeCreditoDTO = planDeCreditoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planDeCreditoDTO);
    }
}
