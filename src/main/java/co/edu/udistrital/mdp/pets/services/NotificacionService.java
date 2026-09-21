package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.NotificacionEntity;
import co.edu.udistrital.mdp.pets.repositories.NotificacionRepository;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public NotificacionEntity createNotificacion(NotificacionEntity notificacion) {
        return notificacionRepository.save(notificacion);
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

    public NotificacionEntity updateNotificacion(Long id, NotificacionEntity notificacion) {
        NotificacionEntity notificacionEntity = getNotificacion(id);
        if (notificacionEntity == null) {
            return null;
        }
        notificacion.setId(id);
        return notificacionRepository.save(notificacion);
    }

    public void deleteNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }
}