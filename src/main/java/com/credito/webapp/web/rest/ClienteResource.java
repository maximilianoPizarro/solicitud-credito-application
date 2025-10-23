package com.credito.webapp.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import com.credito.webapp.service.ClienteService;
import com.credito.webapp.service.dto.ClienteDTO;
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
 * REST controller for managing {@link com.credito.webapp.domain.Cliente}.
 */
@Path("/api/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ClienteResource {

    private final Logger log = LoggerFactory.getLogger(ClienteResource.class);

    private static final String ENTITY_NAME = "cliente";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    ClienteService clienteService;

    /**
     * {@code POST  /clientes} : Create a new cliente.
     *
     * @param clienteDTO the clienteDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new clienteDTO, or with status {@code 400 (Bad Request)} if the cliente has already an ID.
     */
    @POST
    public Response createCliente(@Valid ClienteDTO clienteDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Cliente : {}", clienteDTO);
        if (clienteDTO.id != null) {
            throw new BadRequestAlertException("A new cliente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = clienteService.persistOrUpdate(clienteDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /clientes} : Updates an existing cliente.
     *
     * @param clienteDTO the clienteDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated clienteDTO,
     * or with status {@code 400 (Bad Request)} if the clienteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clienteDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateCliente(@Valid ClienteDTO clienteDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Cliente : {}", clienteDTO);
        if (clienteDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = clienteService.persistOrUpdate(clienteDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /clientes/:id} : delete the "id" cliente.
     *
     * @param id the id of the clienteDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCliente(@PathParam("id") Long id) {
        log.debug("REST request to delete Cliente : {}", id);
        clienteService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /clientes} : get all the clientes.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of clientes in body.
     */
    @GET
    public List<ClienteDTO> getAllClientes() {
        log.debug("REST request to get all Clientes");
        return clienteService.findAll();
    }

    /**
     * {@code GET  /clientes/:id} : get the "id" cliente.
     *
     * @param id the id of the clienteDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the clienteDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getCliente(@PathParam("id") Long id) {
        log.debug("REST request to get Cliente : {}", id);
        Optional<ClienteDTO> clienteDTO = clienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clienteDTO);
    }
}
