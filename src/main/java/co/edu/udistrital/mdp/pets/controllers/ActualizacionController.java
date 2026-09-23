package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.ActualizacionEntity;
import co.edu.udistrital.mdp.pets.services.ActualizacionService;

@RestController
@RequestMapping("/actualizaciones")
public class ActualizacionController {

    private final ActualizacionService actualizacionService;

    public ActualizacionController(ActualizacionService actualizacionService) {
        this.actualizacionService = actualizacionService;
    }

    @GetMapping
    public List<ActualizacionEntity> getActualizaciones() {
        return actualizacionService.getActualizaciones();
    }

    @GetMapping("/{id}")
    public ActualizacionEntity getActualizacion(@PathVariable Long id) {
        return actualizacionService.getActualizacion(id);
    }

    @GetMapping("/tipo/{tipo}")
    public List<ActualizacionEntity> getActualizacionesByTipo(@PathVariable String tipo) {
        return actualizacionService.getActualizacionesByTipo(tipo);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<ActualizacionEntity> getActualizacionesByMascota(@PathVariable Long mascotaId) {
        return actualizacionService.getActualizacionesByMascota(mascotaId);
    }

    @GetMapping("/adoptante/{adoptanteId}")
    public List<ActualizacionEntity> getActualizacionesByAdoptante(@PathVariable Long adoptanteId) {
        return actualizacionService.getActualizacionesByAdoptante(adoptanteId);
    }

    @PostMapping
    public ActualizacionEntity createActualizacion(@RequestBody ActualizacionEntity actualizacion) {
        return actualizacionService.createActualizacion(actualizacion);
    }

    @PutMapping("/{id}")
    public ActualizacionEntity updateActualizacion(@PathVariable Long id,
            @RequestBody ActualizacionEntity actualizacion) {
        return actualizacionService.updateActualizacion(id, actualizacion);
    }

    @DeleteMapping("/{id}")
    public void deleteActualizacion(@PathVariable Long id) {
        actualizacionService.deleteActualizacion(id);
    }
}
