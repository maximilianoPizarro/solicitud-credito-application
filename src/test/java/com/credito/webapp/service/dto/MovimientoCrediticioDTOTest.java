package com.credito.webapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class MovimientoCrediticioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimientoCrediticioDTO.class);
        MovimientoCrediticioDTO movimientoCrediticioDTO1 = new MovimientoCrediticioDTO();
        movimientoCrediticioDTO1.id = 1L;
        MovimientoCrediticioDTO movimientoCrediticioDTO2 = new MovimientoCrediticioDTO();
        assertThat(movimientoCrediticioDTO1).isNotEqualTo(movimientoCrediticioDTO2);
        movimientoCrediticioDTO2.id = movimientoCrediticioDTO1.id;
        assertThat(movimientoCrediticioDTO1).isEqualTo(movimientoCrediticioDTO2);
        movimientoCrediticioDTO2.id = 2L;
        assertThat(movimientoCrediticioDTO1).isNotEqualTo(movimientoCrediticioDTO2);
        movimientoCrediticioDTO1.id = null;
        assertThat(movimientoCrediticioDTO1).isNotEqualTo(movimientoCrediticioDTO2);
    }
}
