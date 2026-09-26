package co.edu.udistrital.mdp.pets.dto;

import java.sql.Date;

import lombok.Data;

/**
 * DTO plano de Refugio (sin colecciones de mascotas/veterinarios) para
 * evitar ciclos de serializacion al convertir con ModelMapper.
 */
@Data
public class RefugioDTO {

    private Long id;
    private String nombre;
    private String ciudad;
    private String direccion;
    private String telefono;
    private String email;
    private String descripcion;
    private Date fechaRegistro;
}
