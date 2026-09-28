package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PruebaConvivenciaDTO {

    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer duracionDias;
    private String estado;
    private String observacion;
    private AdopcionDTO adopcion;

}