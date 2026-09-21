package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.PruebaConvivenciaEntity;
import co.edu.udistrital.mdp.pets.repositories.PruebaConvivenciaRepository;

@Service
public class PruebaConvivenciaService {

    private final PruebaConvivenciaRepository pruebaRepository;

    public PruebaConvivenciaService(PruebaConvivenciaRepository pruebaRepository) {
        this.pruebaRepository = pruebaRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public PruebaConvivenciaEntity createPrueba(PruebaConvivenciaEntity prueba) {
        try {
            return pruebaRepository.save(prueba);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear la prueba de convivencia.", e);
        }
    }

    public List<PruebaConvivenciaEntity> getPruebas() {
        return pruebaRepository.findAll();
    }

    public PruebaConvivenciaEntity getPrueba(Long id) {
        return pruebaRepository.findById(id).orElse(null);
    }

    public List<PruebaConvivenciaEntity> getPruebasByEstado(String estado) {
        return pruebaRepository.findByEstado(estado);
    }

    public PruebaConvivenciaEntity getPruebaByAdopcion(Long adopcionId) {
        return pruebaRepository.findByAdopcionId(adopcionId).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public PruebaConvivenciaEntity updatePrueba(Long id, PruebaConvivenciaEntity prueba) {
        try {
            PruebaConvivenciaEntity entity = getPrueba(id);

            if (entity == null) {
                throw new IllegalArgumentException("La prueba de convivencia no existe.");
            }

            prueba.setId(id);
            return pruebaRepository.save(prueba);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar la prueba de convivencia.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePrueba(Long id) {
        try {
            pruebaRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar la prueba de convivencia.", e);
        }
    }
}