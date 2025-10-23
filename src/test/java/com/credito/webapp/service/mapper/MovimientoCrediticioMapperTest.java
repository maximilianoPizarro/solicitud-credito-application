package com.credito.webapp.service.mapper;

import static com.credito.webapp.domain.MovimientoCrediticioAsserts.*;
import static com.credito.webapp.domain.MovimientoCrediticioTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class MovimientoCrediticioMapperTest {

    @Inject
    MovimientoCrediticioMapper movimientoCrediticioMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMovimientoCrediticioSample1();
        var actual = movimientoCrediticioMapper.toEntity(movimientoCrediticioMapper.toDto(expected));
        assertMovimientoCrediticioAllPropertiesEquals(expected, actual);
    }
}
