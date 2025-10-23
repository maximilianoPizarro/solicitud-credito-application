package com.credito.webapp.service.mapper;

import static com.credito.webapp.domain.ClienteAsserts.*;
import static com.credito.webapp.domain.ClienteTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ClienteMapperTest {

    @Inject
    ClienteMapper clienteMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClienteSample1();
        var actual = clienteMapper.toEntity(clienteMapper.toDto(expected));
        assertClienteAllPropertiesEquals(expected, actual);
    }
}
