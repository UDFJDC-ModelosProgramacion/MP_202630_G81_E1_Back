package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

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
 * Entidad de registro para los eventos de la vida de una mascota en especifico
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


@Data
@Entity
public abstract class EventoVidaEntity extends BaseEntity {
    
    private String tipo;

    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    private String descripcion;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

}
