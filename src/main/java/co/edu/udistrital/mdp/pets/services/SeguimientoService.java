package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.SeguimientoEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.SeguimientoRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarioRepository;

/**
 * Clase que implementa la logica de negocio para la entidad Seguimiento.
 *
 * SeguimientoRepository, MascotaRepository y VeterinarioRepository se
 * reciben por inyeccion de dependencias via constructor.
 */
@Service
public class SeguimientoService {

    private static final String ESTADO_COMPLETADO = "Completado";

    private final SeguimientoRepository seguimientoRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;

    public SeguimientoService(SeguimientoRepository seguimientoRepository,
            MascotaRepository mascotaRepository,
            VeterinarioRepository veterinarioRepository) {
        this.seguimientoRepository = seguimientoRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    /**
     * Reglas de negocio validadas:
     * - La fecha de asignacion no puede ser nula.
     * - Si hay proxima cita, no puede ser anterior a la fecha de asignacion.
     * - La mascota asociada debe existir.
     * - El veterinario asociado debe existir.
     */
    @Transactional
    public SeguimientoEntity createSeguimiento(SeguimientoEntity seguimiento)
            throws EntityNotFoundException, IllegalOperationException {
        validarFechas(seguimiento);
        seguimiento.setMascota(obtenerMascotaValida(seguimiento));
        seguimiento.setVeterinario(obtenerVeterinarioValido(seguimiento));
        return seguimientoRepository.save(seguimiento);
    }

    @Transactional
    public List<SeguimientoEntity> getSeguimientos() {
        return seguimientoRepository.findAll();
    }

    @Transactional
    public SeguimientoEntity getSeguimiento(Long id) throws EntityNotFoundException {
        SeguimientoEntity seguimiento = seguimientoRepository.findById(id).orElse(null);
        if (seguimiento == null) {
            throw new EntityNotFoundException("El seguimiento con el id dado no existe");
        }
        return seguimiento;
    }

    /**
     * Reglas de negocio validadas:
     * - El seguimiento debe existir.
     * - No se puede modificar un seguimiento que ya esta en estado "Completado".
     * - Las mismas reglas de fechas y de existencia de mascota/veterinario que en la creacion.
     */
    @Transactional
    public SeguimientoEntity updateSeguimiento(Long id, SeguimientoEntity seguimiento)
            throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity actual = getSeguimiento(id);
        if (ESTADO_COMPLETADO.equalsIgnoreCase(actual.getEstado())) {
            throw new IllegalOperationException("No se puede modificar un seguimiento que ya esta completado");
        }
        validarFechas(seguimiento);
        seguimiento.setMascota(obtenerMascotaValida(seguimiento));
        seguimiento.setVeterinario(obtenerVeterinarioValido(seguimiento));
        seguimiento.setId(id);
        return seguimientoRepository.save(seguimiento);
    }

    /**
     * Reglas de negocio validadas:
     * - El seguimiento debe existir.
     * - No se puede eliminar un seguimiento que ya este en estado "Completado"
     *   (se conserva el historial medico de la mascota).
     */
    @Transactional
    public void deleteSeguimiento(Long id) throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity seguimiento = getSeguimiento(id);
        if (ESTADO_COMPLETADO.equalsIgnoreCase(seguimiento.getEstado())) {
            throw new IllegalOperationException("No se puede eliminar un seguimiento que ya esta completado");
        }
        seguimientoRepository.delete(seguimiento);
    }

    private void validarFechas(SeguimientoEntity seguimiento) throws IllegalOperationException {
        if (seguimiento.getFechaAsignacion() == null) {
            throw new IllegalOperationException("La fecha de asignacion del seguimiento no puede ser nula");
        }
        if (seguimiento.getProximaCita() != null
                && seguimiento.getProximaCita().before(seguimiento.getFechaAsignacion())) {
            throw new IllegalOperationException("La proxima cita no puede ser anterior a la fecha de asignacion");
        }
    }

    private MascotaEntity obtenerMascotaValida(SeguimientoEntity seguimiento)
            throws EntityNotFoundException, IllegalOperationException {
        if (seguimiento.getMascota() == null || seguimiento.getMascota().getId() == null) {
            throw new IllegalOperationException("El seguimiento debe estar asociado a una mascota");
        }
        MascotaEntity mascota = mascotaRepository.findById(seguimiento.getMascota().getId()).orElse(null);
        if (mascota == null) {
            throw new EntityNotFoundException("La mascota asociada al seguimiento no existe");
        }
        return mascota;
    }

    private VeterinarioEntity obtenerVeterinarioValido(SeguimientoEntity seguimiento)
            throws EntityNotFoundException, IllegalOperationException {
        if (seguimiento.getVeterinario() == null || seguimiento.getVeterinario().getId() == null) {
            throw new IllegalOperationException("El seguimiento debe estar asociado a un veterinario");
        }
        VeterinarioEntity veterinario = veterinarioRepository.findById(seguimiento.getVeterinario().getId())
                .orElse(null);
        if (veterinario == null) {
            throw new EntityNotFoundException("El veterinario asociado al seguimiento no existe");
        }
        return veterinario;
    }
}
