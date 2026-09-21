package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;

@Service
public class SolicitudAdopcionService {

    private final SolicitudAdopcionRepository solicitudRepository;

    public SolicitudAdopcionService(SolicitudAdopcionRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public SolicitudAdopcionEntity createSolicitud(SolicitudAdopcionEntity solicitud) {
        try {
            return solicitudRepository.save(solicitud);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear la solicitud de adopción.", e);
        }
    }

    public List<SolicitudAdopcionEntity> getSolicitudes() {
        return solicitudRepository.findAll();
    }

    public SolicitudAdopcionEntity getSolicitud(Long id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    public List<SolicitudAdopcionEntity> getSolicitudesByEstado(String estado) {
        return solicitudRepository.findByEstado(estado);
    }

    public List<SolicitudAdopcionEntity> getSolicitudesByAdoptante(Long adoptanteId) {
        return solicitudRepository.findByAdoptanteId(adoptanteId);
    }

    public List<SolicitudAdopcionEntity> getSolicitudesByMascota(Long mascotaId) {
        return solicitudRepository.findByMascotaId(mascotaId);
    }

    @Transactional(rollbackFor = Exception.class)
    public SolicitudAdopcionEntity updateSolicitud(Long id, SolicitudAdopcionEntity solicitud) {
        try {
            SolicitudAdopcionEntity entity = getSolicitud(id);

            if (entity == null) {
                throw new IllegalArgumentException("La solicitud de adopción no existe.");
            }

            solicitud.setId(id);
            return solicitudRepository.save(solicitud);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar la solicitud de adopción.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSolicitud(Long id) {
        try {
            solicitudRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar la solicitud de adopción.", e);
        }
    }
}