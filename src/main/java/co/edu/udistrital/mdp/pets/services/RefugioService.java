package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.RefugioRepository;

/**
 * Clase que implementa la logica de negocio para la entidad Refugio.
 *
 * El RefugioRepository se recibe por INYECCION DE DEPENDENCIAS VIA
 * CONSTRUCTOR (nunca se crea con "new" ni se inyecta directamente en el
 * campo con @Autowired), tal como se explico en la actividad de
 * Inyeccion de dependencias: es Spring quien decide y entrega la
 * implementacion concreta del repositorio.
 */
@Service
public class RefugioService {

    private final RefugioRepository refugioRepository;

    public RefugioService(RefugioRepository refugioRepository) {
        this.refugioRepository = refugioRepository;
    }

    /**
     * Reglas de negocio validadas:
     * - El nombre del refugio no puede ser nulo ni vacio.
     * - No pueden existir dos refugios con el mismo nombre.
     */
    @Transactional
    public RefugioEntity createRefugio(RefugioEntity refugio) throws IllegalOperationException {
        validarNombre(refugio);
        if (refugioRepository.findByNombre(refugio.getNombre()) != null) {
            throw new IllegalOperationException("Ya existe un refugio con ese nombre");
        }
        return refugioRepository.save(refugio);
    }

    @Transactional
    public List<RefugioEntity> getRefugios() {
        return refugioRepository.findAll();
    }

    @Transactional
    public RefugioEntity getRefugio(Long id) throws EntityNotFoundException {
        RefugioEntity refugio = refugioRepository.findById(id).orElse(null);
        if (refugio == null) {
            throw new EntityNotFoundException("El refugio con el id dado no existe");
        }
        return refugio;
    }

    /**
     * Reglas de negocio validadas:
     * - El refugio debe existir.
     * - El nombre no puede quedar vacio.
     * - El nuevo nombre no puede coincidir con el de otro refugio distinto.
     */
    @Transactional
    public RefugioEntity updateRefugio(Long id, RefugioEntity refugio)
            throws EntityNotFoundException, IllegalOperationException {
        getRefugio(id);
        validarNombre(refugio);

        RefugioEntity existente = refugioRepository.findByNombre(refugio.getNombre());
        if (existente != null && !existente.getId().equals(id)) {
            throw new IllegalOperationException("Ya existe un refugio con ese nombre");
        }
        refugio.setId(id);
        return refugioRepository.save(refugio);
    }

    /**
     * Reglas de negocio validadas:
     * - El refugio debe existir.
     * - No se puede eliminar un refugio que tenga mascotas registradas.
     */
    @Transactional
    public void deleteRefugio(Long id) throws EntityNotFoundException, IllegalOperationException {
        RefugioEntity refugio = getRefugio(id);
        if (refugio.getMascotas() != null && !refugio.getMascotas().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar un refugio que tiene mascotas registradas");
        }
        refugioRepository.delete(refugio);
    }

    private void validarNombre(RefugioEntity refugio) throws IllegalOperationException {
        if (refugio.getNombre() == null || refugio.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre del refugio no puede ser vacio");
        }
    }
}
