package com.credito.webapp.domain;

import static com.credito.webapp.domain.ClienteTestSamples.*;
import static com.credito.webapp.domain.CuentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.id = cliente1.id;
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }
}
