package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

@Data
public class FotografiaDTO {
    private Long id;
    private String url;
    private boolean principal;
    private String descripcion;
    private Long mascotaId;
}
