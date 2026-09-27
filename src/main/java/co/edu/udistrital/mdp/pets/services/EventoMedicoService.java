package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.EventoMedicoEntity;
import co.edu.udistrital.mdp.pets.repositories.EventoMedicoRepository;

@Service
public class EventoMedicoService {

    private final EventoMedicoRepository eventoMedicoRepository;

    public EventoMedicoService(EventoMedicoRepository eventoMedicoRepository) {
        this.eventoMedicoRepository = eventoMedicoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public EventoMedicoEntity createEventoMedico(EventoMedicoEntity eventoMedico) {
        try {
            return eventoMedicoRepository.save(eventoMedico);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear el evento médico.", e);
        }
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

    @Transactional(rollbackFor = Exception.class)
    public EventoMedicoEntity updateEventoMedico(Long id, EventoMedicoEntity eventoMedico) {
        try {
            EventoMedicoEntity entity = getEventoMedico(id);

            if (entity == null) {
                throw new IllegalArgumentException("El evento médico no existe.");
            }
            eventoMedico.setId(id);
            return eventoMedicoRepository.save(eventoMedico);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar el evento médico.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteEventoMedico(Long id) {
        try {
            eventoMedicoRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar el evento médico.", e);
        }
    }
}