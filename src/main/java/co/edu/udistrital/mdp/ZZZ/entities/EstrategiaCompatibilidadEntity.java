package entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad que almacena los criterios de compatibilidad de una mascota para adopción.
 */
@Data
@Entity
public class EstrategiaCompatibilidadEntity extends BaseEntity {

    private Boolean compatibleConNinos;

    private Boolean compatibleConOtrasMascotas;

    private String nivelActividad;

    private String espacioRequerido;

    private String observaciones;

    @PodamExclude
    @OneToOne
    private MascotaEntity mascota;
}