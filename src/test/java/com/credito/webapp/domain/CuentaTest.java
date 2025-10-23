package com.credito.webapp.domain;

import static com.credito.webapp.domain.ClienteTestSamples.*;
import static com.credito.webapp.domain.CuentaTestSamples.*;
import static com.credito.webapp.domain.MovimientoCrediticioTestSamples.*;
import static com.credito.webapp.domain.PlanDeCreditoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class CuentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuenta.class);
        Cuenta cuenta1 = getCuentaSample1();
        Cuenta cuenta2 = new Cuenta();
        assertThat(cuenta1).isNotEqualTo(cuenta2);

        cuenta2.id = cuenta1.id;
        assertThat(cuenta1).isEqualTo(cuenta2);

        cuenta2 = getCuentaSample2();
        assertThat(cuenta1).isNotEqualTo(cuenta2);
    }
}
