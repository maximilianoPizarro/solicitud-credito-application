package com.credito.webapp.domain;

import com.credito.webapp.domain.enumeration.TipoDocumento;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa al cliente del banco.
 */
@Entity
@Table(name = "cliente")
@RegisterForReflection
public class Cliente extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "numero_cliente", nullable = false, unique = true)
    public Long numeroCliente;

    @NotNull
    @Column(name = "nombre", nullable = false)
    public String nombre;

    @NotNull
    @Column(name = "apellido", nullable = false)
    public String apellido;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    public LocalDate fechaNacimiento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    public TipoDocumento tipoDocumento;

    @NotNull
    @Column(name = "numero_documento", nullable = false, unique = true)
    public String numeroDocumento;

    @Column(name = "email", unique = true)
    public String email;

    @Column(name = "telefono")
    public String telefono;

    @OneToMany(mappedBy = "cliente")
    public Set<Cuenta> cuentas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Cliente{" +
            "id=" +
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

    public Cliente update() {
        return update(this);
    }

    public Cliente persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Cliente update(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("cliente can't be null");
        }
        var entity = Cliente.<Cliente>findById(cliente.id);
        if (entity != null) {
            entity.numeroCliente = cliente.numeroCliente;
            entity.nombre = cliente.nombre;
            entity.apellido = cliente.apellido;
            entity.fechaNacimiento = cliente.fechaNacimiento;
            entity.tipoDocumento = cliente.tipoDocumento;
            entity.numeroDocumento = cliente.numeroDocumento;
            entity.email = cliente.email;
            entity.telefono = cliente.telefono;
            entity.cuentas = cliente.cuentas;
        }
        return entity;
    }

    public static Cliente persistOrUpdate(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("cliente can't be null");
        }
        if (cliente.id == null) {
            persist(cliente);
            return cliente;
        } else {
            return update(cliente);
        }
    }
}
