package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.RetornoEntity;
import co.edu.udistrital.mdp.pets.repositories.RetornoRepository;

@Service
public class RetornoService {

    private final RetornoRepository retornoRepository;

    public RetornoService(RetornoRepository retornoRepository) {
        this.retornoRepository = retornoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public RetornoEntity createRetorno(RetornoEntity retorno) {
        try {
            return retornoRepository.save(retorno);
        } catch (Exception e) {
            throw new IllegalStateException("Error al registrar el retorno.", e);
        }
    }

    public List<RetornoEntity> getRetornos() {
        return retornoRepository.findAll();
    }

    public RetornoEntity getRetorno(Long id) {
        return retornoRepository.findById(id).orElse(null);
    }

    public List<RetornoEntity> getRetornosCompatibles(Boolean compatibleReAdopcion) {
        return retornoRepository.findByCompatibleReAdopcion(compatibleReAdopcion);
    }

    public RetornoEntity getRetornoByAdopcion(Long adopcionId) {
        return retornoRepository.findByAdopcionId(adopcionId).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public RetornoEntity updateRetorno(Long id, RetornoEntity retorno) {
        try {
            RetornoEntity entity = getRetorno(id);

            if (entity == null) {
                throw new IllegalArgumentException("El retorno no existe.");
            }

            retorno.setId(id);
            return retornoRepository.save(retorno);

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar el retorno.", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRetorno(Long id) {
        try {
            retornoRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar el retorno.", e);
        }
    }
}