package co.edu.udistrital.mdp.pets.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.RegistroVacunacionEntity;
import co.edu.udistrital.mdp.pets.entities.VacunaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.RegistroVacunacionRepository;
import co.edu.udistrital.mdp.pets.repositories.VacunaRepository;
import lombok.RequiredArgsConstructor;

/**
 * Clase que implementa la lógica de negocio para la entidad
 * RegistroVacunacion.
 */
@Service
@RequiredArgsConstructor
public class RegistroVacunacionService {

    private static final String REGISTRO_NO_ENCONTRADO =
            "El registro de vacunación con el id dado no fue encontrado";

    private static final String VACUNA_NO_EXISTE =
            "La vacuna asociada al registro no existe";

    private static final String MASCOTA_NO_EXISTE =
            "La mascota asociada al registro no existe";

    private final RegistroVacunacionRepository registroVacunacionRepository;
    private final VacunaRepository vacunaRepository;
    private final MascotaRepository mascotaRepository;

    @Transactional
    public RegistroVacunacionEntity createRegistroVacunacion(
            RegistroVacunacionEntity registro)
            throws IllegalOperationException, EntityNotFoundException {

        validarDatosBasicos(registro);
        validarVacunaYMascota(registro);

        return registroVacunacionRepository.save(registro);
    }

    @Transactional
    public List<RegistroVacunacionEntity> getRegistrosVacunacion() {
        return registroVacunacionRepository.findAll();
    }

    @Transactional
    public RegistroVacunacionEntity getRegistroVacunacion(Long id)
            throws EntityNotFoundException {

        return buscarRegistro(id);
    }

    @Transactional
    public RegistroVacunacionEntity updateRegistroVacunacion(
            Long id,
            RegistroVacunacionEntity registro)
            throws EntityNotFoundException, IllegalOperationException {

        buscarRegistro(id);

        validarDatosBasicos(registro);
        validarVacunaYMascota(registro);

        registro.setId(id);

        return registroVacunacionRepository.save(registro);
    }

    @Transactional
    public void deleteRegistroVacunacion(Long id)
            throws EntityNotFoundException {

        RegistroVacunacionEntity registro = buscarRegistro(id);
        registroVacunacionRepository.delete(registro);
    }

    /**
     * Busca un registro de vacunación y lanza una excepción específica
     * si no existe.
     */
    private RegistroVacunacionEntity buscarRegistro(Long id)
            throws EntityNotFoundException {

        return registroVacunacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        REGISTRO_NO_ENCONTRADO));
    }

    /**
     * Valida los datos básicos del registro de vacunación.
     */
    private void validarDatosBasicos(
            RegistroVacunacionEntity registro)
            throws IllegalOperationException {

        if (registro.getFechaAplicacion() == null) {
            throw new IllegalOperationException(
                    "La fecha de aplicación es obligatoria");
        }

        if (registro.getFechaAplicacion().after(new Date())) {
            throw new IllegalOperationException(
                    "La fecha de aplicación no puede ser futura");
        }

        if (registro.getProximaFecha() != null
                && !registro.getProximaFecha()
                        .after(registro.getFechaAplicacion())) {

            throw new IllegalOperationException(
                    "La próxima fecha debe ser posterior a la fecha de aplicación");
        }

        if (registro.getNumeroLote() == null
                || registro.getNumeroLote().isBlank()) {

            throw new IllegalOperationException(
                    "El número de lote es obligatorio");
        }
    }

    /**
     * Valida que el registro tenga una vacuna y una mascota existentes.
     */
    private void validarVacunaYMascota(
            RegistroVacunacionEntity registro)
            throws IllegalOperationException, EntityNotFoundException {

        VacunaEntity vacuna = obtenerVacuna(registro);
        MascotaEntity mascota = obtenerMascota(registro);

        registro.setVacuna(vacuna);
        registro.setMascota(mascota);
    }

    /**
     * Obtiene la vacuna asociada al registro.
     */
    private VacunaEntity obtenerVacuna(
            RegistroVacunacionEntity registro)
            throws IllegalOperationException, EntityNotFoundException {

        if (registro.getVacuna() == null
                || registro.getVacuna().getId() == null) {

            throw new IllegalOperationException(
                    "El registro debe estar asociado a una vacuna");
        }

        return vacunaRepository.findById(registro.getVacuna().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        VACUNA_NO_EXISTE));
    }

    /**
     * Obtiene la mascota asociada al registro.
     */
    private MascotaEntity obtenerMascota(
            RegistroVacunacionEntity registro)
            throws IllegalOperationException, EntityNotFoundException {

        if (registro.getMascota() == null
                || registro.getMascota().getId() == null) {

            throw new IllegalOperationException(
                    "El registro debe estar asociado a una mascota");
        }

        return mascotaRepository.findById(registro.getMascota().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        MASCOTA_NO_EXISTE));
    }
}
