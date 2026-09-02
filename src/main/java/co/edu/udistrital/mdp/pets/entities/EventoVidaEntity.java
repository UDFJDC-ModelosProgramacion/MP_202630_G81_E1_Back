package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Entidad de registro para los eventos de la vida de una mascota en especifico
 * 
 *
 * @author Samuel Leonardo Acosta Cruz
 */


 @Data
 @MappedSuperclass
public abstract class EventoVidaEntity extends BaseEntity {
    
    // Attributes

    @PodamExclude
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String tipo;
    private String fecha;
    private String descripcion;

    //@ManyToOne
    //private MascotaEntity mascota;

    // Getters and Setters

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }


    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
