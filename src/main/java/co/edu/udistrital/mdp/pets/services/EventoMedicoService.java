package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.EventoMedicoEntity;
import co.edu.udistrital.mdp.pets.repositories.EventoMedicoRepository;

@Service
public class EventoMedicoService {

    private final EventoMedicoRepository eventoMedicoRepository;

    EventoMedicoService(EventoMedicoRepository eventoMedicoRepository) {
        this.eventoMedicoRepository = eventoMedicoRepository;
    }

    public EventoMedicoEntity createEventoMedico(EventoMedicoEntity eventoMedico) {
        return eventoMedicoRepository.save(eventoMedico);
    }

    public List<EventoMedicoEntity> getEventosMedicos() {
        return eventoMedicoRepository.findAll();
    }

    public EventoMedicoEntity getEventoMedico(Long id) {
        return eventoMedicoRepository.findById(id).orElse(null);
    }

    public List<EventoMedicoEntity> getEventosMedicosByMascota(Long mascotaId) {
        return eventoMedicoRepository.findByMascotaId(mascotaId);
    }

    public List<EventoMedicoEntity> getEventosMedicosByDiagnostico(String diagnostico) {
        return eventoMedicoRepository.findByDiagnostico(diagnostico);
    }

    public EventoMedicoEntity updateEventoMedico(Long id, EventoMedicoEntity eventoMedico) {
        EventoMedicoEntity eventoMedicoEntity = getEventoMedico(id);
        if (eventoMedicoEntity == null) {
            return null;
        }
        eventoMedico.setId(id);
        return eventoMedicoRepository.save(eventoMedico);
    }

    public void deleteEventoMedico(Long id) {
        eventoMedicoRepository.deleteById(id);
    }
}