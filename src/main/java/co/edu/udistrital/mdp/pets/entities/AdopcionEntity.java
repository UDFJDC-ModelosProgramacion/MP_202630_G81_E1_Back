package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una adopción.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdopcionEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaAdopcion;

    private String estado;

    private String observacion;

    @PodamExclude
    @OneToOne
    private SolicitudAdopcionEntity solicitud;

    @PodamExclude
    @OneToOne(mappedBy = "adopcion")
    private PruebaConvivenciaEntity pruebaConvivencia;

    @PodamExclude
    @OneToOne(mappedBy = "adopcion")
    private RetornoEntity retorno;
}