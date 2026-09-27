package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.RefugioRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarioRepository;

/**
 * Clase que implementa la logica de negocio para la entidad Veterinario.
 *
 * VeterinarioRepository y RefugioRepository se reciben por inyeccion de
 * dependencias via constructor.
 */
@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final RefugioRepository refugioRepository;

    
    public VeterinarioService(VeterinarioRepository veterinarioRepository, RefugioRepository refugioRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.refugioRepository = refugioRepository;
    }

    /**
     * Reglas de negocio validadas:
     * - El nombre y la especialidad no pueden ser vacios.
     * - Si se asocia un refugio, este debe existir.
     */
    @Transactional
    public VeterinarioEntity createVeterinario(VeterinarioEntity veterinario)
            throws EntityNotFoundException, IllegalOperationException {
        validarDatosBasicos(veterinario);
        asociarRefugioSiAplica(veterinario);
        return veterinarioRepository.save(veterinario);
    }

    @Transactional
    public List<VeterinarioEntity> getVeterinarios() {
        return veterinarioRepository.findAll();
    }

    @Transactional
    public VeterinarioEntity getVeterinario(Long id) throws EntityNotFoundException {
        VeterinarioEntity veterinario = veterinarioRepository.findById(id).orElse(null);
        if (veterinario == null) {
            throw new EntityNotFoundException("El veterinario con el id dado no existe");
        }
        return veterinario;
    }

    /**
     * Se validan las mismas reglas que en la creacion, sobre un veterinario existente.
     */
    @Transactional
    public VeterinarioEntity updateVeterinario(Long id, VeterinarioEntity veterinario)
            throws EntityNotFoundException, IllegalOperationException {
        getVeterinario(id);
        validarDatosBasicos(veterinario);
        asociarRefugioSiAplica(veterinario);
        veterinario.setId(id);
        return veterinarioRepository.save(veterinario);
    }

    /**
     * Reglas de negocio validadas:
     * - El veterinario debe existir.
     * - No se puede eliminar un veterinario que tenga seguimientos asignados
     *   (se conserva el historial de atenciones).
     */
    @Transactional
    public void deleteVeterinario(Long id) throws EntityNotFoundException, IllegalOperationException {
        VeterinarioEntity veterinario = getVeterinario(id);
        if (veterinario.getSeguimientos() != null && !veterinario.getSeguimientos().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar un veterinario que tiene seguimientos asignados");
        }
        veterinarioRepository.delete(veterinario);
    }

    private void validarDatosBasicos(VeterinarioEntity veterinario) throws IllegalOperationException {
        if (veterinario.getNombre() == null || veterinario.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre del veterinario no puede ser vacio");
        }
        if (veterinario.getEspecialidad() == null || veterinario.getEspecialidad().trim().isEmpty()) {
            throw new IllegalOperationException("La especialidad del veterinario no puede ser vacia");
        }
    }

    private void asociarRefugioSiAplica(VeterinarioEntity veterinario) throws EntityNotFoundException {
        if (veterinario.getRefugio() != null) {
            RefugioEntity refugio = refugioRepository.findById(veterinario.getRefugio().getId()).orElse(null);
            if (refugio == null) {
                throw new EntityNotFoundException("El refugio asociado al veterinario no existe");
            }
            veterinario.setRefugio(refugio);
        }
    }
}
