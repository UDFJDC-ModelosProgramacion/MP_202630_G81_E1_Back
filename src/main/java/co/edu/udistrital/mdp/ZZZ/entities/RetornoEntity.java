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
 * Entidad que representa el retorno de una mascota al refugio.
 */
@Data
@Entity
public class RetornoEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaRetorno;

    private String motivo;

    private String descripcion;

    @PodamExclude
    @OneToOne
    private AdopcionEntity adopcion;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

    @PodamExclude
    @ManyToOne
    private RefugioEntity refugio;
}