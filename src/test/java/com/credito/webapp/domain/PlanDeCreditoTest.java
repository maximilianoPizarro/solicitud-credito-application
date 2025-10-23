package com.credito.webapp.domain;

import static com.credito.webapp.domain.CuentaTestSamples.*;
import static com.credito.webapp.domain.PlanDeCreditoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class PlanDeCreditoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanDeCredito.class);
        PlanDeCredito planDeCredito1 = getPlanDeCreditoSample1();
        PlanDeCredito planDeCredito2 = new PlanDeCredito();
        assertThat(planDeCredito1).isNotEqualTo(planDeCredito2);

        planDeCredito2.id = planDeCredito1.id;
        assertThat(planDeCredito1).isEqualTo(planDeCredito2);

        planDeCredito2 = getPlanDeCreditoSample2();
        assertThat(planDeCredito1).isNotEqualTo(planDeCredito2);
    }
}
