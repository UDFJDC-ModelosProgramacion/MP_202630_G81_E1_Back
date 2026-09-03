package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad de registro para las fotografias de los animales
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


@Data
@Entity
public abstract class FotografiaEntity extends BaseEntity {
    
    private String url;

    private boolean principal;
    
    private String descripcion;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

}
