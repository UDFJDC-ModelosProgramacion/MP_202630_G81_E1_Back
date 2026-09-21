package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;
import co.edu.udistrital.mdp.pets.services.AdopcionService;

@RestController
@RequestMapping("/adopciones")
public class AdopcionController {

    private final AdopcionService adopcionService;

    public AdopcionController(AdopcionService adopcionService) {
        this.adopcionService = adopcionService;
    }

    @GetMapping
    public List<AdopcionEntity> getAdopciones() {
        return adopcionService.getAdopciones();
    }

    @GetMapping("/{id}")
    public AdopcionEntity getAdopcion(@PathVariable Long id) {
        return adopcionService.getAdopcion(id);
    }

    @GetMapping("/estado/{estado}")
    public List<AdopcionEntity> getAdopcionesByEstado(@PathVariable String estado) {
        return adopcionService.getAdopcionesByEstado(estado);
    }

    @GetMapping("/solicitud/{solicitudId}")
    public AdopcionEntity getAdopcionBySolicitud(@PathVariable Long solicitudId) {
        return adopcionService.getAdopcionBySolicitud(solicitudId);
    }

    @PostMapping
    public AdopcionEntity createAdopcion(@RequestBody AdopcionEntity adopcion) {
        return adopcionService.createAdopcion(adopcion);
    }

    @PutMapping("/{id}")
    public AdopcionEntity updateAdopcion(@PathVariable Long id,
            @RequestBody AdopcionEntity adopcion) {
        return adopcionService.updateAdopcion(id, adopcion);
    }

    @DeleteMapping("/{id}")
    public void deleteAdopcion(@PathVariable Long id) {
        adopcionService.deleteAdopcion(id);
    }
}