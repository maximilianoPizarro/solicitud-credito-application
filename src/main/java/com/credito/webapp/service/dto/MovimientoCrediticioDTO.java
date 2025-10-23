package com.credito.webapp.service.dto;

import com.credito.webapp.domain.enumeration.TipoMovimiento;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import io.swagger.annotations.ApiModel;

/**
 * A DTO for the {@link com.credito.webapp.domain.MovimientoCrediticio} entity.
 */
@ApiModel(description = "Representa una transacción en la cuenta de crédito (un pago, un cargo, una cuota, etc.).")
@RegisterForReflection
public class MovimientoCrediticioDTO implements Serializable {

    public Long id;

    @NotNull
    public ZonedDateTime fechaMovimiento;

    @NotNull
    public TipoMovimiento tipo;

    @NotNull
    public BigDecimal monto;

    public String descripcion;

    public String referenciaExterna;

    public Long cuentaId;
    public String cuenta;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovimientoCrediticioDTO)) {
            return false;
        }

        return id != null && id.equals(((MovimientoCrediticioDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "MovimientoCrediticioDTO{" +
            ", id=" +
            id +
            ", fechaMovimiento='" +
            fechaMovimiento +
            "'" +
            ", tipo='" +
            tipo +
            "'" +
            ", monto=" +
            monto +
            ", descripcion='" +
            descripcion +
            "'" +
            ", referenciaExterna='" +
            referenciaExterna +
            "'" +
            ", cuentaId=" +
            cuentaId +
            ", cuenta='" +
            cuenta +
            "'" +
            "}"
        );
    }
}
