package com.credito.webapp.service.dto;

import com.credito.webapp.domain.enumeration.EstadoCuenta;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link com.credito.webapp.domain.Cuenta} entity.
 */
@ApiModel(
    description = "Representa una instancia específica de un crédito o producto financiero\nque un cliente posee. Es el corazón del historial."
)
@RegisterForReflection
public class CuentaDTO implements Serializable {

    public Long id;

    @NotNull
    public String numeroCuenta;

    @NotNull
    public ZonedDateTime fechaApertura;

    @NotNull
    public BigDecimal montoOtorgado;

    public BigDecimal saldoPendiente;

    @NotNull
    public EstadoCuenta estado;

    public ZonedDateTime fechaCierre;

    public Long clienteId;
    public String cliente;
    public Long planId;
    public String plan;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CuentaDTO)) {
            return false;
        }

        return id != null && id.equals(((CuentaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "CuentaDTO{" +
            ", id=" +
            id +
            ", numeroCuenta='" +
            numeroCuenta +
            "'" +
            ", fechaApertura='" +
            fechaApertura +
            "'" +
            ", montoOtorgado=" +
            montoOtorgado +
            ", saldoPendiente=" +
            saldoPendiente +
            ", estado='" +
            estado +
            "'" +
            ", fechaCierre='" +
            fechaCierre +
            "'" +
            ", clienteId=" +
            clienteId +
            ", cliente='" +
            cliente +
            "'" +
            ", planId=" +
            planId +
            ", plan='" +
            plan +
            "'" +
            "}"
        );
    }
}
