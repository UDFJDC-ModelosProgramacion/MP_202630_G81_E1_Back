package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.NotificacionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.NotificacionRepository;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, MascotaRepository mascotaRepository,
            AdoptanteRepository adoptanteRepository) {
        this.notificacionRepository = notificacionRepository;
        this.mascotaRepository = mascotaRepository;
        this.adoptanteRepository = adoptanteRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public NotificacionEntity createNotificacion(NotificacionEntity notificacion)
            throws EntityNotFoundException, IllegalOperationException {
        if (notificacion.getCanal() == null || notificacion.getCanal().isBlank()) {
            throw new IllegalOperationException("El canal de la notificación no puede ser vacío");
        }
        notificacion.setMascota(resolverMascota(notificacion.getMascota()));
        notificacion.setAdoptante(resolverAdoptanteOpcional(notificacion.getAdoptante()));
        return notificacionRepository.save(notificacion);
    }

    public List<NotificacionEntity> getNotificaciones() {
        return notificacionRepository.findAll();
    }

    public NotificacionEntity getNotificacion(Long id) throws EntityNotFoundException {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La notificación con el id dado no existe"));
    }

    public List<NotificacionEntity> getNotificacionesByCanal(String canal) {
        return notificacionRepository.findByCanal(canal);
    }

    public List<NotificacionEntity> getNotificacionesByMascota(Long mascotaId) {
        return notificacionRepository.findByMascotaId(mascotaId);
    }

    public List<NotificacionEntity> getNotificacionesByAdoptante(Long adoptanteId) {
        return notificacionRepository.findByAdoptanteId(adoptanteId);
    }

    @Transactional(rollbackFor = Exception.class)
    public NotificacionEntity updateNotificacion(Long id, NotificacionEntity nuevosDatos)
            throws EntityNotFoundException, IllegalOperationException {
        NotificacionEntity entity = getNotificacion(id);
        if (nuevosDatos.getCanal() == null || nuevosDatos.getCanal().isBlank()) {
            throw new IllegalOperationException("El canal de la notificación no puede ser vacío");
        }
        entity.setTipo(nuevosDatos.getTipo());
        entity.setMensaje(nuevosDatos.getMensaje());
        entity.setFecha(nuevosDatos.getFecha());
        entity.setCanal(nuevosDatos.getCanal());
        entity.setMascota(resolverMascota(nuevosDatos.getMascota()));
        entity.setAdoptante(resolverAdoptanteOpcional(nuevosDatos.getAdoptante()));
        return notificacionRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteNotificacion(Long id) throws EntityNotFoundException {
        getNotificacion(id);
        notificacionRepository.deleteById(id);
    }

    private MascotaEntity resolverMascota(MascotaEntity mascota) throws EntityNotFoundException {
        if (mascota == null || mascota.getId() == null) {
            throw new EntityNotFoundException("La mascota asociada a la notificación no existe");
        }
        return mascotaRepository.findById(mascota.getId())
                .orElseThrow(() -> new EntityNotFoundException("La mascota asociada a la notificación no existe"));
    }

    private AdoptanteEntity resolverAdoptanteOpcional(AdoptanteEntity adoptante) throws EntityNotFoundException {
        if (adoptante == null || adoptante.getId() == null) {
            return null;
        }
        return adoptanteRepository.findById(adoptante.getId())
                .orElseThrow(() -> new EntityNotFoundException("El adoptante asociado a la notificación no existe"));
    }
}