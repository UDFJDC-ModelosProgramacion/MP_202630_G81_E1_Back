package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.EventoMedicoEntity;
import co.edu.udistrital.mdp.pets.services.EventoMedicoService;

@RestController
@RequestMapping("/eventos-medicos")
public class EventoMedicoController {

    private final EventoMedicoService eventoMedicoService;

    public EventoMedicoController(EventoMedicoService eventoMedicoService) {
        this.eventoMedicoService = eventoMedicoService;
    }

    @GetMapping
    public List<EventoMedicoEntity> getEventosMedicos() {
        return eventoMedicoService.getEventosMedicos();
    }

    @GetMapping("/{id}")
    public EventoMedicoEntity getEventoMedico(@PathVariable Long id) {
        return eventoMedicoService.getEventoMedico(id);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<EventoMedicoEntity> getEventosMedicosByMascota(@PathVariable Long mascotaId) {
        return eventoMedicoService.getEventosMedicosByMascota(mascotaId);
    }

    @GetMapping("/diagnostico/{diagnostico}")
    public List<EventoMedicoEntity> getEventosMedicosByDiagnostico(@PathVariable String diagnostico) {
        return eventoMedicoService.getEventosMedicosByDiagnostico(diagnostico);
    }

    @PostMapping
    public EventoMedicoEntity createEventoMedico(@RequestBody EventoMedicoEntity eventoMedico) {
        return eventoMedicoService.createEventoMedico(eventoMedico);
    }

    @PutMapping("/{id}")
    public EventoMedicoEntity updateEventoMedico(@PathVariable Long id,
            @RequestBody EventoMedicoEntity eventoMedico) {
        return eventoMedicoService.updateEventoMedico(id, eventoMedico);
    }

    @DeleteMapping("/{id}")
    public void deleteEventoMedico(@PathVariable Long id) {
        eventoMedicoService.deleteEventoMedico(id);
    }
}