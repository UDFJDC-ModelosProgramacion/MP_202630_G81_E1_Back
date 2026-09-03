package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad de contencion de los mensajes
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


@Data
@Entity
@MappedSuperclass
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
    @ManyToAny
    private ActualizacionEntity actualizacion;
}