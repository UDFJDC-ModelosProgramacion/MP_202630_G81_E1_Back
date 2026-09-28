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

	private final VacunaRepository vacunaRepository;

	public VacunaService(VacunaRepository vacunaRepository) {
		this.vacunaRepository = vacunaRepository;
	}

	@Transactional
	public VacunaEntity createVacuna(VacunaEntity vacuna) throws IllegalOperationException {
		if (vacuna.getNombre() == null || vacuna.getNombre().isBlank()) {
			throw new IllegalOperationException("El nombre de la vacuna no puede estar vacío");
		}
		if (vacunaRepository.findByNombreIgnoreCase(vacuna.getNombre()).isPresent()) {
        throw new IllegalOperationException("Ya existe una vacuna con el nombre indicado");
        }
		return vacunaRepository.save(vacuna);
	}

	@Transactional
	public List<VacunaEntity> getVacunas() {
		return vacunaRepository.findAll();
	}

	@Transactional
	public VacunaEntity getVacuna(Long id) throws EntityNotFoundException {
		Optional<VacunaEntity> vacuna = vacunaRepository.findById(id);
		if (vacuna.isEmpty()) {
			throw new EntityNotFoundException("La vacuna con el id dado no fue encontrada");
		}
		return vacuna.get();
	}

	@Transactional
	public VacunaEntity updateVacuna(Long id, VacunaEntity vacuna)
			throws EntityNotFoundException, IllegalOperationException {
		Optional<VacunaEntity> existente = vacunaRepository.findById(id);
		if (existente.isEmpty()) {
			throw new EntityNotFoundException("La vacuna con el id dado no fue encontrada");
		}
		if (vacuna.getNombre() == null || vacuna.getNombre().isBlank()) {
			throw new IllegalOperationException("El nombre de la vacuna no puede estar vacío");
		}
		Optional<VacunaEntity> conMismoNombre = vacunaRepository.findByNombreIgnoreCase(vacuna.getNombre());
        if (conMismoNombre.isPresent() && !conMismoNombre.get().getId().equals(id)) {
        throw new IllegalOperationException("Ya existe una vacuna con el nombre indicado");
        }
		vacuna.setId(id);
		return vacunaRepository.save(vacuna);
	}

	@Transactional
	public void deleteVacuna(Long id) throws EntityNotFoundException, IllegalOperationException {
		Optional<VacunaEntity> vacuna = vacunaRepository.findById(id);
		if (vacuna.isEmpty()) {
			throw new EntityNotFoundException("La vacuna con el id dado no fue encontrada");
		}
		if (!vacuna.get().getRegistrosVacunacion().isEmpty()) {
			throw new IllegalOperationException(
					"No se puede eliminar la vacuna porque tiene registros de vacunación asociados");
		}
		vacunaRepository.deleteById(id);
	}
}