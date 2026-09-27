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
 * El RefugioRepository se recibe por inyeccion de dependencias mediante
 * constructor.
 */
@Service
public class RefugioService {

    private final RefugioRepository refugioRepository;

    public RefugioService(RefugioRepository refugioRepository) {
        this.refugioRepository = refugioRepository;
    }

    /**
     * Crea un refugio.
     *
     * Reglas de negocio:
     * - El nombre del refugio no puede ser nulo ni vacio.
     * - No pueden existir dos refugios con el mismo nombre.
     */
    @Transactional
    public RefugioEntity createRefugio(RefugioEntity refugio)
            throws IllegalOperationException {

        validarNombre(refugio);

        if (refugioRepository.findByNombre(refugio.getNombre()) != null) {
            throw new IllegalOperationException(
                    "Ya existe un refugio con ese nombre");
        }

        return refugioRepository.save(refugio);
    }

    /**
     * Obtiene todos los refugios.
     */
    @Transactional
    public List<RefugioEntity> getRefugios() {
        return refugioRepository.findAll();
    }

    /**
     * Obtiene un refugio por su identificador.
     */
    @Transactional
    public RefugioEntity getRefugio(Long id)
            throws EntityNotFoundException {

        return refugioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El refugio con el id dado no existe"));
    }

    /**
     * Actualiza un refugio.
     *
     * Reglas de negocio:
     * - El refugio debe existir.
     * - El nombre no puede quedar vacio.
     * - El nuevo nombre no puede coincidir con el de otro refugio distinto.
     */
    @Transactional
    public RefugioEntity updateRefugio(
            Long id,
            RefugioEntity refugio)
            throws EntityNotFoundException, IllegalOperationException {

        // Se consulta directamente el repositorio para evitar una llamada
        // interna a otro metodo @Transactional.
        RefugioEntity existentePorId = refugioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El refugio con el id dado no existe"));

        validarNombre(refugio);

        RefugioEntity existentePorNombre =
                refugioRepository.findByNombre(refugio.getNombre());

        if (existentePorNombre != null
                && !existentePorNombre.getId().equals(id)) {
            throw new IllegalOperationException(
                    "Ya existe un refugio con ese nombre");
        }

        existentePorId.setNombre(refugio.getNombre());

        return refugioRepository.save(existentePorId);
    }

    /**
     * Elimina un refugio.
     *
     * Reglas de negocio:
     * - El refugio debe existir.
     * - No se puede eliminar un refugio que tenga mascotas registradas.
     */
    @Transactional
    public void deleteRefugio(Long id)
            throws EntityNotFoundException, IllegalOperationException {

        // Se consulta directamente el repositorio para evitar una llamada
        // interna a getRefugio(), que es un metodo @Transactional.
        RefugioEntity refugio = refugioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El refugio con el id dado no existe"));

        if (refugio.getMascotas() != null
                && !refugio.getMascotas().isEmpty()) {
            throw new IllegalOperationException(
                    "No se puede eliminar un refugio que tiene mascotas registradas");
        }

        refugioRepository.delete(refugio);
    }

    /**
     * Valida el nombre del refugio.
     */
    private void validarNombre(RefugioEntity refugio)
            throws IllegalOperationException {

        if (refugio.getNombre() == null
                || refugio.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException(
                    "El nombre del refugio no puede ser vacio");
        }
    }
}
