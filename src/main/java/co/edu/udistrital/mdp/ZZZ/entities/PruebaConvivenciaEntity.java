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
 * Entidad que representa el periodo de prueba de convivencia entre una mascota y un adoptante.
 */
@Data
@Entity
public class PruebaConvivenciaEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    private String estado;

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
}