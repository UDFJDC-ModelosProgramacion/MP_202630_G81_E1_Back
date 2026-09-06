package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una solicitud de adopción.
 */
@Data
@Entity
public class SolicitudAdopcionEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private String estado;

    private String tipoSolicitud;

    private String observacion;

    @PodamExclude
    @ManyToOne
    private AdoptanteEntity adoptante;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

    @PodamExclude
    @OneToOne(mappedBy = "solicitud")
    private AdopcionEntity adopcion;

}