package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.ResenaEntity;
import co.edu.udistrital.mdp.pets.services.ResenaService;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public List<ResenaEntity> getResenas() {
        return resenaService.getResenas();
    }

    @GetMapping("/{id}")
    public ResenaEntity getResena(@PathVariable Long id) {
        return resenaService.getResena(id);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<ResenaEntity> getResenasByMascota(@PathVariable Long mascotaId) {
        return resenaService.getResenasByMascota(mascotaId);
    }

    @GetMapping("/adoptante/{adoptanteId}")
    public List<ResenaEntity> getResenasByAdoptante(@PathVariable Long adoptanteId) {
        return resenaService.getResenasByAdoptante(adoptanteId);
    }

    @GetMapping("/calificacion/{calificacion}")
    public List<ResenaEntity> getResenasByCalificacion(@PathVariable Integer calificacion) {
        return resenaService.getResenasByCalificacion(calificacion);
    }

    @PostMapping
    public ResenaEntity createResena(@RequestBody ResenaEntity resena) {
        return resenaService.createResena(resena);
    }

    @PutMapping("/{id}")
    public ResenaEntity updateResena(@PathVariable Long id,
            @RequestBody ResenaEntity resena) {
        return resenaService.updateResena(id, resena);
    }

    @DeleteMapping("/{id}")
    public void deleteResena(@PathVariable Long id) {
        resenaService.deleteResena(id);
    }
}