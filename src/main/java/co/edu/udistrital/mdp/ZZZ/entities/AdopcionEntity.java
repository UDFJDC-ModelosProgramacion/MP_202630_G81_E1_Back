package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que representa una adopción exitosa.
 */
@Data
@Entity
public class AdopcionEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaAdopcion;

    private Boolean activa;

    private String observaciones;

    @PodamExclude
    @OneToOne
    private SolicitudAdopcionEntity solicitud;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

    @PodamExclude
    @ManyToOne
    private AdoptanteEntity adoptante;

    @PodamExclude
    @ManyToOne
    private VeterinarioEntity veterinarioResponsable;
}