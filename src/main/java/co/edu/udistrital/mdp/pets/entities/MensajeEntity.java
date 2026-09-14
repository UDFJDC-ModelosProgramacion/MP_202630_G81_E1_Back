package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad de contencion de los mensajes
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class MensajeEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private String asunto;
    private String contenido;

    private boolean leido;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

    @PodamExclude
    @ManyToOne
    private ActualizacionEntity actualizacion;

    @PodamExclude
    @ManyToOne
    private AdoptanteEntity adoptante;
}
