package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.EventoMedicoEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.EventoMedicoRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@Service
public class EventoMedicoService {

    private final EventoMedicoRepository eventoMedicoRepository;
    private final MascotaRepository mascotaRepository;

    public EventoMedicoService(EventoMedicoRepository eventoMedicoRepository, MascotaRepository mascotaRepository) {
        this.eventoMedicoRepository = eventoMedicoRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public EventoMedicoEntity createEventoMedico(EventoMedicoEntity eventoMedico)
            throws EntityNotFoundException, IllegalOperationException {
        if (eventoMedico.getDiagnostico() == null || eventoMedico.getDiagnostico().isBlank()) {
            throw new IllegalOperationException("El diagnóstico del evento médico no puede ser vacío");
        }
        eventoMedico.setMascota(resolverMascota(eventoMedico.getMascota()));
        return eventoMedicoRepository.save(eventoMedico);
    }

    public List<EventoMedicoEntity> getEventosMedicos() {
        return eventoMedicoRepository.findAll();
    }

    public EventoMedicoEntity getEventoMedico(Long id) throws EntityNotFoundException {
        return eventoMedicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El evento médico con el id dado no existe"));
    }

    public List<EventoMedicoEntity> getEventosMedicosByMascota(Long mascotaId) {
        return eventoMedicoRepository.findByMascotaId(mascotaId);
    }

    public List<EventoMedicoEntity> getEventosMedicosByDiagnostico(String diagnostico) {
        return eventoMedicoRepository.findByDiagnostico(diagnostico);
    }

    @Transactional(rollbackFor = Exception.class)
    public EventoMedicoEntity updateEventoMedico(Long id, EventoMedicoEntity nuevosDatos)
            throws EntityNotFoundException, IllegalOperationException {
        EventoMedicoEntity entity = getEventoMedico(id);
        if (nuevosDatos.getDiagnostico() == null || nuevosDatos.getDiagnostico().isBlank()) {
            throw new IllegalOperationException("El diagnóstico del evento médico no puede ser vacío");
        }
        entity.setFecha(nuevosDatos.getFecha());
        entity.setDescripcion(nuevosDatos.getDescripcion());
        entity.setDiagnostico(nuevosDatos.getDiagnostico());
        entity.setTratamiento(nuevosDatos.getTratamiento());
        entity.setMascota(resolverMascota(nuevosDatos.getMascota()));
        return eventoMedicoRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteEventoMedico(Long id) throws EntityNotFoundException {
        getEventoMedico(id);
        eventoMedicoRepository.deleteById(id);
    }

    private MascotaEntity resolverMascota(MascotaEntity mascota) throws EntityNotFoundException {
        if (mascota == null || mascota.getId() == null) {
            throw new EntityNotFoundException("La mascota asociada al evento médico no existe");
        }
        return mascotaRepository.findById(mascota.getId())
                .orElseThrow(() -> new EntityNotFoundException("La mascota asociada al evento médico no existe"));
    }
}