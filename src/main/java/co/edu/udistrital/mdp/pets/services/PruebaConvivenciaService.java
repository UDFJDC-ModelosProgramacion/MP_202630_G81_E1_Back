package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import co.edu.udistrital.mdp.pets.entities.PruebaConvivenciaEntity;
import co.edu.udistrital.mdp.pets.repositories.PruebaConvivenciaRepository;

@Service
public class PruebaConvivenciaService {

    private final PruebaConvivenciaRepository pruebaRepository;

    PruebaConvivenciaService(PruebaConvivenciaRepository pruebaRepository) {
        this.pruebaRepository = pruebaRepository;
    }

    public PruebaConvivenciaEntity createPrueba(PruebaConvivenciaEntity prueba) {
        return pruebaRepository.save(prueba);
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

    public PruebaConvivenciaEntity updatePrueba(Long id, PruebaConvivenciaEntity prueba) {
        PruebaConvivenciaEntity pruebaEntity = getPrueba(id);

        if (pruebaEntity == null) {
            return null;
        }

        prueba.setId(id);
        return pruebaRepository.save(prueba);
    }

    public void deletePrueba(Long id) {
        pruebaRepository.deleteById(id);
    }
}