package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.VacunaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VacunaRepository;

/**
 * Clase que implementa la lógica de negocio para la entidad Vacuna.
 */
@Service
public class VacunaService {

    private static final String VACUNA_NO_ENCONTRADA =
            "La vacuna con el id dado no fue encontrada";

    private static final String NOMBRE_VACUNA_INVALIDO =
            "El nombre de la vacuna no puede estar vacío";

    private static final String NOMBRE_VACUNA_DUPLICADO =
            "Ya existe una vacuna con el nombre indicado";

    private static final String VACUNA_CON_REGISTROS =
            "No se puede eliminar la vacuna porque tiene registros de vacunación asociados";

    private final VacunaRepository vacunaRepository;

    public VacunaService(VacunaRepository vacunaRepository) {
        this.vacunaRepository = vacunaRepository;
    }

    @Transactional
    public VacunaEntity createVacuna(VacunaEntity vacuna)
            throws IllegalOperationException {

        validarNombre(vacuna);
        validarNombreUnico(vacuna.getNombre(), null);

        return vacunaRepository.save(vacuna);
    }

    @Transactional
    public List<VacunaEntity> getVacunas() {
        return vacunaRepository.findAll();
    }

    @Transactional
    public VacunaEntity getVacuna(Long id)
            throws EntityNotFoundException {

        return obtenerVacuna(id);
    }

    @Transactional
    public VacunaEntity updateVacuna(
            Long id,
            VacunaEntity vacuna)
            throws EntityNotFoundException, IllegalOperationException {

        obtenerVacuna(id);

        validarNombre(vacuna);
        validarNombreUnico(vacuna.getNombre(), id);

        vacuna.setId(id);

        return vacunaRepository.save(vacuna);
    }

    @Transactional
    public void deleteVacuna(Long id)
            throws EntityNotFoundException, IllegalOperationException {

        VacunaEntity vacuna = obtenerVacuna(id);

        if (vacuna.getRegistrosVacunacion() != null
                && !vacuna.getRegistrosVacunacion().isEmpty()) {

            throw new IllegalOperationException(VACUNA_CON_REGISTROS);
        }

        vacunaRepository.delete(vacuna);
    }

    /**
     * Valida que la vacuna exista y la devuelve.
     */
    private VacunaEntity obtenerVacuna(Long id)
            throws EntityNotFoundException {

        Optional<VacunaEntity> vacuna = vacunaRepository.findById(id);

        if (vacuna.isEmpty()) {
            throw new EntityNotFoundException(VACUNA_NO_ENCONTRADA);
        }

        return vacuna.get();
    }

    /**
     * Valida que el nombre de la vacuna sea válido.
     */
    private void validarNombre(VacunaEntity vacuna)
            throws IllegalOperationException {

        if (vacuna == null
                || vacuna.getNombre() == null
                || vacuna.getNombre().isBlank()) {

            throw new IllegalOperationException(NOMBRE_VACUNA_INVALIDO);
        }
    }

    /**
     * Valida que no exista otra vacuna con el mismo nombre.
     *
     * @param nombre nombre que se quiere validar
     * @param idVacunaActual ID de la vacuna que se está actualizando.
     *                       Es null cuando se está creando.
     */
    private void validarNombreUnico(
            String nombre,
            Long idVacunaActual)
            throws IllegalOperationException {

        Optional<VacunaEntity> vacunaExistente =
                vacunaRepository.findByNombreIgnoreCase(nombre);

        if (vacunaExistente.isEmpty()) {
            return;
        }

        VacunaEntity vacuna = vacunaExistente.get();

        if (idVacunaActual == null
                || !vacuna.getId().equals(idVacunaActual)) {

            throw new IllegalOperationException(NOMBRE_VACUNA_DUPLICADO);
        }
    }
}
