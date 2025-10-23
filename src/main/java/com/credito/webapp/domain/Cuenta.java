package com.credito.webapp.domain;

import com.credito.webapp.domain.enumeration.EstadoCuenta;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa una instancia específica de un crédito o producto financiero
 * que un cliente posee. Es el corazón del historial.
 */
@Entity
@Table(name = "cuenta")
@RegisterForReflection
public class Cuenta extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "numero_cuenta", nullable = false, unique = true)
    public String numeroCuenta;

    @NotNull
    @Column(name = "fecha_apertura", nullable = false)
    public ZonedDateTime fechaApertura;

    @NotNull
    @Column(name = "monto_otorgado", precision = 21, scale = 2, nullable = false)
    public BigDecimal montoOtorgado;

    @Column(name = "saldo_pendiente", precision = 21, scale = 2)
    public BigDecimal saldoPendiente;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    public EstadoCuenta estado;

    @Column(name = "fecha_cierre")
    public ZonedDateTime fechaCierre;

    @OneToMany(mappedBy = "cuenta")
    public Set<MovimientoCrediticio> movimientos = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    public Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    public PlanDeCredito plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cuenta)) {
            return false;
        }
        return id != null && id.equals(((Cuenta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Cuenta{" +
            "id=" +
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
            "}"
        );
    }

    public Cuenta update() {
        return update(this);
    }

    public Cuenta persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Cuenta update(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("cuenta can't be null");
        }
        var entity = Cuenta.<Cuenta>findById(cuenta.id);
        if (entity != null) {
            entity.numeroCuenta = cuenta.numeroCuenta;
            entity.fechaApertura = cuenta.fechaApertura;
            entity.montoOtorgado = cuenta.montoOtorgado;
            entity.saldoPendiente = cuenta.saldoPendiente;
            entity.estado = cuenta.estado;
            entity.fechaCierre = cuenta.fechaCierre;
            entity.movimientos = cuenta.movimientos;
            entity.cliente = cuenta.cliente;
            entity.plan = cuenta.plan;
        }
        return entity;
    }

    public static Cuenta persistOrUpdate(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("cuenta can't be null");
        }
        if (cuenta.id == null) {
            persist(cuenta);
            return cuenta;
        } else {
            return update(cuenta);
        }
    }
}
