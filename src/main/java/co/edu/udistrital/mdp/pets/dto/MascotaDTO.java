package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

/**
 * DTO de Mascota. El campo "refugio" solo necesita traer el id
 * (los demas campos son opcionales) para que el controller pueda
 * asociarla al refugio correspondiente antes de llamar al Service.
 */
@Data
public class MascotaDTO {

    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private String tamano;
    private String temperamento;
    private String necesidadesEspecificas;
    private String requisitoEspacio;
    private Boolean compatibleConNinos;
    private Boolean compatibleConMascotas;
    private String nivelActividad;
    private String estado;
    private RefugioDTO refugio;
}
