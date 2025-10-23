package com.credito.webapp.service.dto;

import com.credito.webapp.config.Constants;
import com.credito.webapp.domain.enumeration.TipoDocumento;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;

/**
 * A DTO for the {@link com.credito.webapp.domain.Cliente} entity.
 */
@ApiModel(description = "Representa al cliente del banco.")
@RegisterForReflection
public class ClienteDTO implements Serializable {

    public Long id;

    @NotNull
    public Long numeroCliente;

    @NotNull
    public String nombre;

    @NotNull
    public String apellido;

    @NotNull
    @JsonbDateFormat(value = Constants.LOCAL_DATE_FORMAT)
    public LocalDate fechaNacimiento;

    @NotNull
    public TipoDocumento tipoDocumento;

    @NotNull
    public String numeroDocumento;

    public String email;

    public String telefono;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        return id != null && id.equals(((ClienteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ClienteDTO{" +
            ", id=" +
            id +
            ", numeroCliente=" +
            numeroCliente +
            ", nombre='" +
            nombre +
            "'" +
            ", apellido='" +
            apellido +
            "'" +
            ", fechaNacimiento='" +
            fechaNacimiento +
            "'" +
            ", tipoDocumento='" +
            tipoDocumento +
            "'" +
            ", numeroDocumento='" +
            numeroDocumento +
            "'" +
            ", email='" +
            email +
            "'" +
            ", telefono='" +
            telefono +
            "'" +
            "}"
        );
    }
}
