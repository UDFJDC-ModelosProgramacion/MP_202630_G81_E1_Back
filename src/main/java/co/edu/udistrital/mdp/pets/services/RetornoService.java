package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.stereotype.Service;
import co.edu.udistrital.mdp.pets.entities.RetornoEntity;
import co.edu.udistrital.mdp.pets.repositories.RetornoRepository;

@Service
public class RetornoService {

    private final RetornoRepository retornoRepository;

    RetornoService(RetornoRepository retornoRepository) {
        this.retornoRepository = retornoRepository;
    }

    public RetornoEntity createRetorno(RetornoEntity retorno) {
        return retornoRepository.save(retorno);
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

    public RetornoEntity updateRetorno(Long id, RetornoEntity retorno) {
        RetornoEntity retornoEntity = getRetorno(id);

        if (retornoEntity == null) {
            return null;
        }

        retorno.setId(id);
        return retornoRepository.save(retorno);
    }

    public void deleteRetorno(Long id) {
        retornoRepository.deleteById(id);
    }
}