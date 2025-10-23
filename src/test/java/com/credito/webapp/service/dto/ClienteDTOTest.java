package com.credito.webapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteDTO.class);
        ClienteDTO clienteDTO1 = new ClienteDTO();
        clienteDTO1.id = 1L;
        ClienteDTO clienteDTO2 = new ClienteDTO();
        assertThat(clienteDTO1).isNotEqualTo(clienteDTO2);
        clienteDTO2.id = clienteDTO1.id;
        assertThat(clienteDTO1).isEqualTo(clienteDTO2);
        clienteDTO2.id = 2L;
        assertThat(clienteDTO1).isNotEqualTo(clienteDTO2);
        clienteDTO1.id = null;
        assertThat(clienteDTO1).isNotEqualTo(clienteDTO2);
    }
}
