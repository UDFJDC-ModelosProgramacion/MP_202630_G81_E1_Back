package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.ResenaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.ResenaRepository;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;

    public ResenaService(ResenaRepository resenaRepository, MascotaRepository mascotaRepository,
            AdoptanteRepository adoptanteRepository) {
        this.resenaRepository = resenaRepository;
        this.mascotaRepository = mascotaRepository;
        this.adoptanteRepository = adoptanteRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResenaEntity createResena(ResenaEntity resena) throws EntityNotFoundException, IllegalOperationException {
        if (resena.getCalificacion() == null || resena.getCalificacion() < 1 || resena.getCalificacion() > 5) {
            throw new IllegalOperationException("La calificación de la reseña debe estar entre 1 y 5");
        }
        resena.setMascota(resolverMascota(resena.getMascota()));
        resena.setAdoptante(resolverAdoptanteOpcional(resena.getAdoptante()));
        return resenaRepository.save(resena);
    }

    public List<ResenaEntity> getResenas() {
        return resenaRepository.findAll();
    }

    public ResenaEntity getResena(Long id) throws EntityNotFoundException {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La reseña con el id dado no existe"));
    }

    public List<ResenaEntity> getResenasByMascota(Long mascotaId) {
        return resenaRepository.findByMascotaId(mascotaId);
    }

    public List<ResenaEntity> getResenasByAdoptante(Long adoptanteId) {
        return resenaRepository.findByAdoptanteId(adoptanteId);
    }

    public List<ResenaEntity> getResenasByCalificacion(Integer calificacion) {
        return resenaRepository.findByCalificacion(calificacion);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResenaEntity updateResena(Long id, ResenaEntity nuevosDatos)
            throws EntityNotFoundException, IllegalOperationException {
        ResenaEntity entity = getResena(id);
        if (nuevosDatos.getCalificacion() == null || nuevosDatos.getCalificacion() < 1
                || nuevosDatos.getCalificacion() > 5) {
            throw new IllegalOperationException("La calificación de la reseña debe estar entre 1 y 5");
        }
        entity.setCalificacion(nuevosDatos.getCalificacion());
        entity.setComentario(nuevosDatos.getComentario());
        entity.setFecha(nuevosDatos.getFecha());
        entity.setMascota(resolverMascota(nuevosDatos.getMascota()));
        entity.setAdoptante(resolverAdoptanteOpcional(nuevosDatos.getAdoptante()));
        return resenaRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteResena(Long id) throws EntityNotFoundException {
        getResena(id);
        resenaRepository.deleteById(id);
    }

    private MascotaEntity resolverMascota(MascotaEntity mascota) throws EntityNotFoundException {
        if (mascota == null || mascota.getId() == null) {
            throw new EntityNotFoundException("La mascota asociada a la reseña no existe");
        }
        return mascotaRepository.findById(mascota.getId())
                .orElseThrow(() -> new EntityNotFoundException("La mascota asociada a la reseña no existe"));
    }

    private AdoptanteEntity resolverAdoptanteOpcional(AdoptanteEntity adoptante) throws EntityNotFoundException {
        if (adoptante == null || adoptante.getId() == null) {
            return null;
        }
        return adoptanteRepository.findById(adoptante.getId())
                .orElseThrow(() -> new EntityNotFoundException("El adoptante asociado a la reseña no existe"));
    }
}