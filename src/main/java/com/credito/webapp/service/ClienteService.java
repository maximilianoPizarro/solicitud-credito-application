package com.credito.webapp.service;

import com.credito.webapp.domain.Cliente;
import com.credito.webapp.service.dto.ClienteDTO;
import com.credito.webapp.service.mapper.ClienteMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Transactional
public class ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @Inject
    ClienteMapper clienteMapper;

    @Transactional
    public ClienteDTO persistOrUpdate(ClienteDTO clienteDTO) {
        log.debug("Request to save Cliente : {}", clienteDTO);
        var cliente = clienteMapper.toEntity(clienteDTO);
        cliente = Cliente.persistOrUpdate(cliente);
        return clienteMapper.toDto(cliente);
    }

    /**
     * Delete the Cliente by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        Cliente.findByIdOptional(id).ifPresent(cliente -> {
            cliente.delete();
        });
    }

    /**
     * Get one cliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ClienteDTO> findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        return Cliente.findByIdOptional(id).map(cliente -> clienteMapper.toDto((Cliente) cliente));
    }

    /**
     * Get all the clientes.
     * @return the list of entities.
     */
    public List<ClienteDTO> findAll() {
        log.debug("Request to get all Clientes");
        List<Cliente> clientes = Cliente.findAll().list();
        return clienteMapper.toDto(clientes);
    }
}
