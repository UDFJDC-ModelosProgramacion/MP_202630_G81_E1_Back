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
 * Entidad de registro para el registro de la historia de adopciones del animal
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


@Data
@Entity
public class HistoriaExitoEntity {
    
    private String titulo;
    
    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;
}
