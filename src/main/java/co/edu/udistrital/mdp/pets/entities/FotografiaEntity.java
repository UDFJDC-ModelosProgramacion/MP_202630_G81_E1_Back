package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad de registro para las fotografias de los animales
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public abstract class FotografiaEntity extends BaseEntity {
    
    private String url;

    private boolean principal;
    
    private String descripcion;

    @PodamExclude
    @ManyToOne
    private MascotaEntity mascota;

}
