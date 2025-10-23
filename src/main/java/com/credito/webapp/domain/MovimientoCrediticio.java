package com.credito.webapp.domain;

import com.credito.webapp.domain.enumeration.TipoMovimiento;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Representa una transacción en la cuenta de crédito (un pago, un cargo, una cuota, etc.).
 */
@Entity
@Table(name = "movimiento_crediticio")
@RegisterForReflection
public class MovimientoCrediticio extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "fecha_movimiento", nullable = false)
    public ZonedDateTime fechaMovimiento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    public TipoMovimiento tipo;

    @NotNull
    @Column(name = "monto", precision = 21, scale = 2, nullable = false)
    public BigDecimal monto;

    @Column(name = "descripcion")
    public String descripcion;

    @Column(name = "referencia_externa", unique = true)
    public String referenciaExterna;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    public Cuenta cuenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovimientoCrediticio)) {
            return false;
        }
        return id != null && id.equals(((MovimientoCrediticio) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "MovimientoCrediticio{" +
            "id=" +
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
            "}"
        );
    }

    public MovimientoCrediticio update() {
        return update(this);
    }

    public MovimientoCrediticio persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static MovimientoCrediticio update(MovimientoCrediticio movimientoCrediticio) {
        if (movimientoCrediticio == null) {
            throw new IllegalArgumentException("movimientoCrediticio can't be null");
        }
        var entity = MovimientoCrediticio.<MovimientoCrediticio>findById(movimientoCrediticio.id);
        if (entity != null) {
            entity.fechaMovimiento = movimientoCrediticio.fechaMovimiento;
            entity.tipo = movimientoCrediticio.tipo;
            entity.monto = movimientoCrediticio.monto;
            entity.descripcion = movimientoCrediticio.descripcion;
            entity.referenciaExterna = movimientoCrediticio.referenciaExterna;
            entity.cuenta = movimientoCrediticio.cuenta;
        }
        return entity;
    }

    public static MovimientoCrediticio persistOrUpdate(MovimientoCrediticio movimientoCrediticio) {
        if (movimientoCrediticio == null) {
            throw new IllegalArgumentException("movimientoCrediticio can't be null");
        }
        if (movimientoCrediticio.id == null) {
            persist(movimientoCrediticio);
            return movimientoCrediticio;
        } else {
            return update(movimientoCrediticio);
        }
    }
}
