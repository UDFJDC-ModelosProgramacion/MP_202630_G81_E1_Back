package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una solicitud de adopción realizada por un adoptante.
 */
@Data
@Entity
public class SolicitudAdopcionEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;

    private String estado;

    private String comentarios;

    @PodamExclude
    @ManyToOne
    private AdoptanteEntity adoptante;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

    @PodamExclude
    @ManyToOne
    private RefugioEntity refugio;
}