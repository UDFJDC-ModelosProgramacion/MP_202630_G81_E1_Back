package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AdopcionDTO {

    private Long id;
    private Date fechaAdopcion;
    private String estado;
    private String observacion;
    private SolicitudAdopcionDTO solicitud;
    private PruebaConvivenciaDTO pruebaConvivencia;
    private RetornoDTO retorno;

}