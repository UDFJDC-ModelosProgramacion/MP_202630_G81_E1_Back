package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.services.SolicitudAdopcionService;

@RestController
@RequestMapping("/solicitudes-adopcion")
public class SolicitudAdopcionController {

    private final SolicitudAdopcionService solicitudService;

    public SolicitudAdopcionController(SolicitudAdopcionService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public List<SolicitudAdopcionEntity> getSolicitudes() {
        return solicitudService.getSolicitudes();
    }

    @GetMapping("/{id}")
    public SolicitudAdopcionEntity getSolicitud(@PathVariable Long id) {
        return solicitudService.getSolicitud(id);
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudAdopcionEntity> getSolicitudesByEstado(@PathVariable String estado) {
        return solicitudService.getSolicitudesByEstado(estado);
    }

    @GetMapping("/adoptante/{adoptanteId}")
    public List<SolicitudAdopcionEntity> getSolicitudesByAdoptante(@PathVariable Long adoptanteId) {
        return solicitudService.getSolicitudesByAdoptante(adoptanteId);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<SolicitudAdopcionEntity> getSolicitudesByMascota(@PathVariable Long mascotaId) {
        return solicitudService.getSolicitudesByMascota(mascotaId);
    }

    @PostMapping
    public SolicitudAdopcionEntity createSolicitud(@RequestBody SolicitudAdopcionEntity solicitud) {
        return solicitudService.createSolicitud(solicitud);
    }

    @PutMapping("/{id}")
    public SolicitudAdopcionEntity updateSolicitud(@PathVariable Long id,
            @RequestBody SolicitudAdopcionEntity solicitud) {
        return solicitudService.updateSolicitud(id, solicitud);
    }

    @DeleteMapping("/{id}")
    public void deleteSolicitud(@PathVariable Long id) {
        solicitudService.deleteSolicitud(id);
    }
}