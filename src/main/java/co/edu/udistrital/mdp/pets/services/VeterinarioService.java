package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.RefugioRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarioRepository;

/**
 * Clase que implementa la lógica de negocio para la entidad Veterinario.
 *
 * VeterinarioRepository y RefugioRepository se reciben por inyección de
 * dependencias vía constructor.
 */
@Service
public class VeterinarioService {

    private static final String VETERINARIO_NO_EXISTE =
            "El veterinario con el id dado no existe";

    private static final String NOMBRE_INVALIDO =
            "El nombre del veterinario no puede ser vacío";

    private static final String ESPECIALIDAD_INVALIDA =
            "La especialidad del veterinario no puede ser vacía";

    private static final String REFUGIO_NO_EXISTE =
            "El refugio asociado al veterinario no existe";

    private static final String TIENE_SEGUIMIENTOS =
            "No se puede eliminar un veterinario que tiene seguimientos asignados";

    private final VeterinarioRepository veterinarioRepository;
    private final RefugioRepository refugioRepository;

    public VeterinarioService(
            VeterinarioRepository veterinarioRepository,
            RefugioRepository refugioRepository) {

        this.veterinarioRepository = veterinarioRepository;
        this.refugioRepository = refugioRepository;
    }

    /**
     * Crea un veterinario.
     *
     * Reglas:
     * - El nombre no puede ser vacío.
     * - La especialidad no puede ser vacía.
     * - Si se asocia un refugio, este debe existir.
     */
    @Transactional
    public VeterinarioEntity createVeterinario(
            VeterinarioEntity veterinario)
            throws EntityNotFoundException, IllegalOperationException {

        validarDatosBasicos(veterinario);
        asociarRefugioSiAplica(veterinario);

        return veterinarioRepository.save(veterinario);
    }

    /**
     * Obtiene todos los veterinarios.
     */
    @Transactional
    public List<VeterinarioEntity> getVeterinarios() {
        return veterinarioRepository.findAll();
    }

    /**
     * Obtiene un veterinario por su ID.
     */
    @Transactional
    public VeterinarioEntity getVeterinario(Long id)
            throws EntityNotFoundException {

        return obtenerVeterinario(id);
    }

    /**
     * Actualiza un veterinario.
     *
     * Reglas:
     * - El veterinario debe existir.
     * - El nombre no puede ser vacío.
     * - La especialidad no puede ser vacía.
     * - Si se asocia un refugio, este debe existir.
     */
    @Transactional
    public VeterinarioEntity updateVeterinario(
            Long id,
            VeterinarioEntity veterinario)
            throws EntityNotFoundException, IllegalOperationException {

        obtenerVeterinario(id);

        validarDatosBasicos(veterinario);
        asociarRefugioSiAplica(veterinario);

        veterinario.setId(id);

        return veterinarioRepository.save(veterinario);
    }

    /**
     * Elimina un veterinario.
     *
     * Reglas:
     * - El veterinario debe existir.
     * - No se puede eliminar si tiene seguimientos asignados.
     */
    @Transactional
    public void deleteVeterinario(Long id)
            throws EntityNotFoundException, IllegalOperationException {

        VeterinarioEntity veterinario = obtenerVeterinario(id);

        if (tieneSeguimientos(veterinario)) {
            throw new IllegalOperationException(TIENE_SEGUIMIENTOS);
        }

        veterinarioRepository.delete(veterinario);
    }

    /**
     * Busca un veterinario y lanza una excepción si no existe.
     *
     * Este método no tiene @Transactional porque es un método auxiliar
     * utilizado internamente por los métodos transaccionales públicos.
     */
    private VeterinarioEntity obtenerVeterinario(Long id)
            throws EntityNotFoundException {

        Optional<VeterinarioEntity> veterinario =
                veterinarioRepository.findById(id);

        if (veterinario.isEmpty()) {
            throw new EntityNotFoundException(VETERINARIO_NO_EXISTE);
        }

        return veterinario.get();
    }

    /**
     * Valida los datos básicos del veterinario.
     */
    private void validarDatosBasicos(
            VeterinarioEntity veterinario)
            throws IllegalOperationException {

        if (veterinario == null) {
            throw new IllegalOperationException(
                    "El veterinario no puede ser nulo");
        }

        if (veterinario.getNombre() == null
                || veterinario.getNombre().trim().isEmpty()) {

            throw new IllegalOperationException(NOMBRE_INVALIDO);
        }

        if (veterinario.getEspecialidad() == null
                || veterinario.getEspecialidad().trim().isEmpty()) {

            throw new IllegalOperationException(ESPECIALIDAD_INVALIDA);
        }
    }

    /**
     * Asocia un refugio al veterinario cuando se especifica.
     */
    private void asociarRefugioSiAplica(
            VeterinarioEntity veterinario)
            throws EntityNotFoundException {

        if (veterinario.getRefugio() == null) {
            return;
        }

        Long idRefugio = veterinario.getRefugio().getId();

        RefugioEntity refugio = refugioRepository
                .findById(idRefugio)
                .orElseThrow(() ->
                        new EntityNotFoundException(REFUGIO_NO_EXISTE));

        veterinario.setRefugio(refugio);
    }

    /**
     * Determina si el veterinario tiene seguimientos asignados.
     */
    private boolean tieneSeguimientos(
            VeterinarioEntity veterinario) {

        return veterinario.getSeguimientos() != null
                && !veterinario.getSeguimientos().isEmpty();
    }
}
