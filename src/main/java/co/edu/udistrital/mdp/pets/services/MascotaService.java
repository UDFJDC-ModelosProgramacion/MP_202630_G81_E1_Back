package co.edu.udistrital.mdp.pets.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.RefugioRepository;

/**
 * Clase que implementa la logica de negocio para la entidad Mascota.
 *
 * MascotaRepository y RefugioRepository se reciben por inyeccion de
 * dependencias via constructor (no se instancian con "new").
 */
@Service
public class MascotaService {

    private static final List<String> ESTADOS_VALIDOS =
            Arrays.asList("Disponible", "En proceso de adopcion", "Adoptado");

    private final MascotaRepository mascotaRepository;
    private final RefugioRepository refugioRepository;

    @Autowired
    public MascotaService(MascotaRepository mascotaRepository, RefugioRepository refugioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.refugioRepository = refugioRepository;
    }

    /**
     * Reglas de negocio validadas:
     * - El nombre y la especie no pueden ser vacios.
     * - La edad no puede ser negativa.
     * - Si se especifica un estado, debe ser uno de los valores validos.
     * - Si se asocia un refugio, este debe existir.
     */
    @Transactional
    public MascotaEntity createMascota(MascotaEntity mascota)
            throws EntityNotFoundException, IllegalOperationException {
        validarDatosBasicos(mascota);
        asociarRefugioSiAplica(mascota);
        return mascotaRepository.save(mascota);
    }

    @Transactional
    public List<MascotaEntity> getMascotas() {
        return mascotaRepository.findAll();
    }

    @Transactional
    public MascotaEntity getMascota(Long id) throws EntityNotFoundException {
        MascotaEntity mascota = mascotaRepository.findById(id).orElse(null);
        if (mascota == null) {
            throw new EntityNotFoundException("La mascota con el id dado no existe");
        }
        return mascota;
    }

    /**
     * Se validan las mismas reglas que en la creacion, sobre una mascota existente.
     */
    @Transactional
    public MascotaEntity updateMascota(Long id, MascotaEntity mascota)
            throws EntityNotFoundException, IllegalOperationException {
        getMascota(id);
        validarDatosBasicos(mascota);
        asociarRefugioSiAplica(mascota);
        mascota.setId(id);
        return mascotaRepository.save(mascota);
    }

    /**
     * Reglas de negocio validadas:
     * - La mascota debe existir.
     * - No se puede eliminar una mascota que tenga seguimientos veterinarios registrados
     *   (se conserva su historial medico).
     */
    @Transactional
    public void deleteMascota(Long id) throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity mascota = getMascota(id);
        if (mascota.getSeguimientos() != null && !mascota.getSeguimientos().isEmpty()) {
            throw new IllegalOperationException(
                    "No se puede eliminar una mascota que tiene seguimientos veterinarios registrados");
        }
        mascotaRepository.delete(mascota);
    }

    private void validarDatosBasicos(MascotaEntity mascota) throws IllegalOperationException {
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre de la mascota no puede ser vacio");
        }
        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new IllegalOperationException("La especie de la mascota no puede ser vacia");
        }
        if (mascota.getEdad() != null && mascota.getEdad() < 0) {
            throw new IllegalOperationException("La edad de la mascota no puede ser negativa");
        }
        if (mascota.getEstado() != null && !ESTADOS_VALIDOS.contains(mascota.getEstado())) {
            throw new IllegalOperationException("El estado de la mascota no es valido");
        }
    }

    private void asociarRefugioSiAplica(MascotaEntity mascota) throws EntityNotFoundException {
        if (mascota.getRefugio() != null) {
            RefugioEntity refugio = refugioRepository.findById(mascota.getRefugio().getId()).orElse(null);
            if (refugio == null) {
                throw new EntityNotFoundException("El refugio asociado a la mascota no existe");
            }
            mascota.setRefugio(refugio);
        }
    }
}
