package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una prueba de convivencia.
 */
@Data
@Entity
public class PruebaConvivenciaEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    private Integer duracionDias;

    private String estado;

    private String observacion;

    @PodamExclude
    @OneToOne
    private AdopcionEntity adopcion;

}