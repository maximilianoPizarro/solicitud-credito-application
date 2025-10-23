package com.credito.webapp.domain;

import com.credito.webapp.domain.enumeration.TipoPlan;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa un producto o plan de crédito ofrecido por el banco.
 */
@Entity
@Table(name = "plan_de_credito")
@RegisterForReflection
public class PlanDeCredito extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "nombre", nullable = false, unique = true)
    public String nombre;

    @Lob
    @Column(name = "descripcion")
    public String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    public TipoPlan tipo;

    @NotNull
    @Column(name = "tasa_interes", precision = 21, scale = 2, nullable = false)
    public BigDecimal tasaInteres;

    @Column(name = "plazo_maximo")
    public Integer plazoMaximo;

    @OneToMany(mappedBy = "plan")
    public Set<Cuenta> cuentas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanDeCredito)) {
            return false;
        }
        return id != null && id.equals(((PlanDeCredito) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PlanDeCredito{" +
            "id=" +
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

    public PlanDeCredito update() {
        return update(this);
    }

    public PlanDeCredito persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static PlanDeCredito update(PlanDeCredito planDeCredito) {
        if (planDeCredito == null) {
            throw new IllegalArgumentException("planDeCredito can't be null");
        }
        var entity = PlanDeCredito.<PlanDeCredito>findById(planDeCredito.id);
        if (entity != null) {
            entity.nombre = planDeCredito.nombre;
            entity.descripcion = planDeCredito.descripcion;
            entity.tipo = planDeCredito.tipo;
            entity.tasaInteres = planDeCredito.tasaInteres;
            entity.plazoMaximo = planDeCredito.plazoMaximo;
            entity.cuentas = planDeCredito.cuentas;
        }
        return entity;
    }

    public static PlanDeCredito persistOrUpdate(PlanDeCredito planDeCredito) {
        if (planDeCredito == null) {
            throw new IllegalArgumentException("planDeCredito can't be null");
        }
        if (planDeCredito.id == null) {
            persist(planDeCredito);
            return planDeCredito;
        } else {
            return update(planDeCredito);
        }
    }
}
