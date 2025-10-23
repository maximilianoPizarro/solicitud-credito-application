package com.credito.webapp.domain;

import static com.credito.webapp.domain.CuentaTestSamples.*;
import static com.credito.webapp.domain.MovimientoCrediticioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class MovimientoCrediticioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimientoCrediticio.class);
        MovimientoCrediticio movimientoCrediticio1 = getMovimientoCrediticioSample1();
        MovimientoCrediticio movimientoCrediticio2 = new MovimientoCrediticio();
        assertThat(movimientoCrediticio1).isNotEqualTo(movimientoCrediticio2);

        movimientoCrediticio2.id = movimientoCrediticio1.id;
        assertThat(movimientoCrediticio1).isEqualTo(movimientoCrediticio2);

        movimientoCrediticio2 = getMovimientoCrediticioSample2();
        assertThat(movimientoCrediticio1).isNotEqualTo(movimientoCrediticio2);
    }
}
