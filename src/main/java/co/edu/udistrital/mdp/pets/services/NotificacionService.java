package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.NotificacionEntity;
import co.edu.udistrital.mdp.pets.repositories.NotificacionRepository;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public NotificacionEntity createNotificacion(NotificacionEntity notificacion) {
        try {
            return notificacionRepository.save(notificacion);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear la notificación.", e);
        }
    }

    public List<NotificacionEntity> getNotificaciones() {
        return notificacionRepository.findAll();
    }

    public NotificacionEntity getNotificacion(Long id) {
        return notificacionRepository.findById(id).orElse(null);
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
    public NotificacionEntity updateNotificacion(Long id, NotificacionEntity notificacion) {
        try {
            NotificacionEntity entity = getNotificacion(id);

            if (entity == null) {
                throw new IllegalArgumentException("La notificación no existe.");
            }
            notificacion.setId(id);
            return notificacionRepository.save(notificacion);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar la notificación.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteNotificacion(Long id) {
        try {
            notificacionRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar la notificación.", e);
        }
    }
}