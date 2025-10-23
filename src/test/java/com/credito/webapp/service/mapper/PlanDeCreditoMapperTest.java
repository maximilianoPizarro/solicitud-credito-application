package com.credito.webapp.service.mapper;

import static com.credito.webapp.domain.PlanDeCreditoAsserts.*;
import static com.credito.webapp.domain.PlanDeCreditoTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PlanDeCreditoMapperTest {

    @Inject
    PlanDeCreditoMapper planDeCreditoMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanDeCreditoSample1();
        var actual = planDeCreditoMapper.toEntity(planDeCreditoMapper.toDto(expected));
        assertPlanDeCreditoAllPropertiesEquals(expected, actual);
    }
}
