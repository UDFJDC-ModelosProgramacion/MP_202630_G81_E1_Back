package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RetornoDTO {

    private Long id;
    private Date fecha;
    private String motivo;
    private String descripcion;
    private Boolean compatibleReAdopcion;
    private AdopcionDTO adopcion;

}