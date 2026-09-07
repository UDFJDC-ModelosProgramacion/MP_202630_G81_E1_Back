package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa el retorno de una mascota al refugio.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class RetornoEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private String motivo;

    private String descripcion;

    private Boolean compatibleReAdopcion;

    @PodamExclude
    @OneToOne
    private AdopcionEntity adopcion;
}