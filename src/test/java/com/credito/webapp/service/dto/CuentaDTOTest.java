package com.credito.webapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.credito.webapp.TestUtil;
import org.junit.jupiter.api.Test;

class CuentaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuentaDTO.class);
        CuentaDTO cuentaDTO1 = new CuentaDTO();
        cuentaDTO1.id = 1L;
        CuentaDTO cuentaDTO2 = new CuentaDTO();
        assertThat(cuentaDTO1).isNotEqualTo(cuentaDTO2);
        cuentaDTO2.id = cuentaDTO1.id;
        assertThat(cuentaDTO1).isEqualTo(cuentaDTO2);
        cuentaDTO2.id = 2L;
        assertThat(cuentaDTO1).isNotEqualTo(cuentaDTO2);
        cuentaDTO1.id = null;
        assertThat(cuentaDTO1).isNotEqualTo(cuentaDTO2);
    }
}
