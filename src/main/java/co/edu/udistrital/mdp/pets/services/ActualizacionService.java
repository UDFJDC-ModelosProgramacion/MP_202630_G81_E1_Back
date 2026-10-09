package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ActualizacionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ActualizacionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@Service
public class ActualizacionService {

    private final ActualizacionRepository actualizacionRepository;
    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;

    public ActualizacionService(ActualizacionRepository actualizacionRepository,
            MascotaRepository mascotaRepository, AdoptanteRepository adoptanteRepository) {
        this.actualizacionRepository = actualizacionRepository;
        this.mascotaRepository = mascotaRepository;
        this.adoptanteRepository = adoptanteRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ActualizacionEntity createActualizacion(ActualizacionEntity actualizacion)
            throws EntityNotFoundException, IllegalOperationException {
        if (actualizacion.getTipo() == null || actualizacion.getTipo().isBlank()) {
            throw new IllegalOperationException("El tipo de la actualización no puede ser vacío");
        }
        MascotaEntity mascota = resolverMascota(actualizacion.getMascota());
        AdoptanteEntity adoptante = resolverAdoptanteOpcional(actualizacion.getAdoptante());
        actualizacion.setMascota(mascota);
        actualizacion.setAdoptante(adoptante);
        return actualizacionRepository.save(actualizacion);
    }

    public List<ActualizacionEntity> getActualizaciones() {
        return actualizacionRepository.findAll();
    }

    public ActualizacionEntity getActualizacion(Long id) throws EntityNotFoundException {
        return actualizacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La actualización con el id dado no existe"));
    }

    public List<ActualizacionEntity> getActualizacionesByTipo(String tipo) {
        return actualizacionRepository.findByTipo(tipo);
    }

    public List<ActualizacionEntity> getActualizacionesByMascota(Long mascotaId) {
        return actualizacionRepository.findByMascotaId(mascotaId);
    }

    public List<ActualizacionEntity> getActualizacionesByAdoptante(Long adoptanteId) {
        return actualizacionRepository.findByAdoptanteId(adoptanteId);
    }

    @Transactional(rollbackFor = Exception.class)
    public ActualizacionEntity updateActualizacion(Long id, ActualizacionEntity nuevosDatos)
            throws EntityNotFoundException, IllegalOperationException {
        ActualizacionEntity entity = getActualizacion(id);
        if (nuevosDatos.getTipo() == null || nuevosDatos.getTipo().isBlank()) {
            throw new IllegalOperationException("El tipo de la actualización no puede ser vacío");
        }
        entity.setTipo(nuevosDatos.getTipo());
        entity.setDescripcion(nuevosDatos.getDescripcion());
        entity.setArchivoUrl(nuevosDatos.getArchivoUrl());
        entity.setFecha(nuevosDatos.getFecha());
        entity.setMascota(resolverMascota(nuevosDatos.getMascota()));
        entity.setAdoptante(resolverAdoptanteOpcional(nuevosDatos.getAdoptante()));
        return actualizacionRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteActualizacion(Long id) throws EntityNotFoundException {
        getActualizacion(id);
        actualizacionRepository.deleteById(id);
    }

    private MascotaEntity resolverMascota(MascotaEntity mascota) throws EntityNotFoundException {
        if (mascota == null || mascota.getId() == null) {
            throw new EntityNotFoundException("La mascota asociada a la actualización no existe");
        }
        return mascotaRepository.findById(mascota.getId())
                .orElseThrow(() -> new EntityNotFoundException("La mascota asociada a la actualización no existe"));
    }

    private AdoptanteEntity resolverAdoptanteOpcional(AdoptanteEntity adoptante) throws EntityNotFoundException {
        if (adoptante == null || adoptante.getId() == null) {
            return null;
        }
        return adoptanteRepository.findById(adoptante.getId())
                .orElseThrow(() -> new EntityNotFoundException("El adoptante asociado a la actualización no existe"));
    }
}