package com.credito.webapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class PlanDeCreditoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanDeCreditoDTO.class);
        PlanDeCreditoDTO planDeCreditoDTO1 = new PlanDeCreditoDTO();
        planDeCreditoDTO1.id = 1L;
        PlanDeCreditoDTO planDeCreditoDTO2 = new PlanDeCreditoDTO();
        assertThat(planDeCreditoDTO1).isNotEqualTo(planDeCreditoDTO2);
        planDeCreditoDTO2.id = planDeCreditoDTO1.id;
        assertThat(planDeCreditoDTO1).isEqualTo(planDeCreditoDTO2);
        planDeCreditoDTO2.id = 2L;
        assertThat(planDeCreditoDTO1).isNotEqualTo(planDeCreditoDTO2);
        planDeCreditoDTO1.id = null;
        assertThat(planDeCreditoDTO1).isNotEqualTo(planDeCreditoDTO2);
    }
}
