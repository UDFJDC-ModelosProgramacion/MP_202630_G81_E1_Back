package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;

@Service
public class SolicitudAdopcionService {

    private final SolicitudAdopcionRepository solicitudRepository;

    SolicitudAdopcionService(SolicitudAdopcionRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public SolicitudAdopcionEntity createSolicitud(SolicitudAdopcionEntity solicitud) {
        return solicitudRepository.save(solicitud);
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

    public SolicitudAdopcionEntity updateSolicitud(Long id, SolicitudAdopcionEntity solicitud) {
        SolicitudAdopcionEntity solicitudEntity = getSolicitud(id);

        if (solicitudEntity == null) {
            return null;
        }

        solicitud.setId(id);
        return solicitudRepository.save(solicitud);
    }

    public void deleteSolicitud(Long id) {
        solicitudRepository.deleteById(id);
    }
}