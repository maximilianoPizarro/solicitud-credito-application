package com.credito.webapp.service.mapper;

import static com.credito.webapp.domain.CuentaAsserts.*;
import static com.credito.webapp.domain.CuentaTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CuentaMapperTest {

    @Inject
    CuentaMapper cuentaMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCuentaSample1();
        var actual = cuentaMapper.toEntity(cuentaMapper.toDto(expected));
        assertCuentaAllPropertiesEquals(expected, actual);
    }
}
