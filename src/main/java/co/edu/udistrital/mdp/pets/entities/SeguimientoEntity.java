package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Represents a follow-up / checkup record for a pet, assigned to a
 * veterinarian.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seguimiento")
public class SeguimientoEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaAsignacion;

    @Temporal(TemporalType.DATE)
    private Date proximaCita;

    private String observacion;
    private String estado;

    // many Seguimiento are performed by one Veterinario
    @PodamExclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id")
    private VeterinarioEntity veterinario;

    // many Seguimiento belong to one Mascota
    @PodamExclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id")
    private MascotaEntity mascota;

}
