package com.credito.webapp.service.dto;

import com.credito.webapp.domain.enumeration.TipoPlan;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
/**
 * A DTO for the {@link com.credito.webapp.domain.PlanDeCredito} entity.
 */
@ApiModel(description = "Representa un producto o plan de crédito ofrecido por el banco.")
@RegisterForReflection
public class PlanDeCreditoDTO implements Serializable {

    public Long id;

    @NotNull
    public String nombre;

    @Lob
    public String descripcion;

    @NotNull
    public TipoPlan tipo;

    @NotNull
    public BigDecimal tasaInteres;

    public Integer plazoMaximo;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanDeCreditoDTO)) {
            return false;
        }

        return id != null && id.equals(((PlanDeCreditoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PlanDeCreditoDTO{" +
            ", id=" +
            id +
            ", nombre='" +
            nombre +
            "'" +
            ", descripcion='" +
            descripcion +
            "'" +
            ", tipo='" +
            tipo +
            "'" +
            ", tasaInteres=" +
            tasaInteres +
            ", plazoMaximo=" +
            plazoMaximo +
            "}"
        );
    }
}
