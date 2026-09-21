package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;

/**
 * Clase que implementa la lógica de negocio para la entidad Adoptante.
 */
@Service
public class AdoptanteService {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

	@Autowired
	private AdoptanteRepository adoptanteRepository;

	@Transactional
	public AdoptanteEntity createAdoptante(AdoptanteEntity adoptante) throws IllegalOperationException {
		validarDatosBasicos(adoptante);
		validarEmailUnico(adoptante, null);
		return adoptanteRepository.save(adoptante);
	}

	@Transactional
	public List<AdoptanteEntity> getAdoptantes() {
		return adoptanteRepository.findAll();
	}

	@Transactional
	public AdoptanteEntity getAdoptante(Long id) throws EntityNotFoundException {
		Optional<AdoptanteEntity> adoptante = adoptanteRepository.findById(id);
		if (adoptante.isEmpty()) {
			throw new EntityNotFoundException("El adoptante con el id dado no fue encontrado");
		}
		return adoptante.get();
	}

	@Transactional
	public AdoptanteEntity updateAdoptante(Long id, AdoptanteEntity adoptante)
			throws EntityNotFoundException, IllegalOperationException {
		Optional<AdoptanteEntity> existente = adoptanteRepository.findById(id);
		if (existente.isEmpty()) {
			throw new EntityNotFoundException("El adoptante con el id dado no fue encontrado");
		}
		validarDatosBasicos(adoptante);
		validarEmailUnico(adoptante, id);
		adoptante.setId(id);
		return adoptanteRepository.save(adoptante);
	}

	@Transactional
	public void deleteAdoptante(Long id) throws EntityNotFoundException, IllegalOperationException {
		Optional<AdoptanteEntity> adoptante = adoptanteRepository.findById(id);
		if (adoptante.isEmpty()) {
			throw new EntityNotFoundException("El adoptante con el id dado no fue encontrado");
		}
		if (!adoptante.get().getSolicitudesAdopcion().isEmpty()) {
			throw new IllegalOperationException(
					"No se puede eliminar el adoptante porque tiene solicitudes de adopción asociadas");
		}
		adoptanteRepository.deleteById(id);
	}

	private void validarDatosBasicos(AdoptanteEntity adoptante) throws IllegalOperationException {
		if (adoptante.getNombre() == null || adoptante.getNombre().isBlank()) {
			throw new IllegalOperationException("El nombre del adoptante no puede estar vacío");
		}
		if (adoptante.getEmail() == null || !EMAIL_PATTERN.matcher(adoptante.getEmail()).matches()) {
			throw new IllegalOperationException("El correo electrónico del adoptante no es válido");
		}
	}

	private void validarEmailUnico(AdoptanteEntity adoptante, Long idActual) throws IllegalOperationException {
    Optional<AdoptanteEntity> conMismoEmail = adoptanteRepository.findByEmailIgnoreCase(adoptante.getEmail());
    if (conMismoEmail.isPresent() && !conMismoEmail.get().getId().equals(idActual)) {
        throw new IllegalOperationException("Ya existe un adoptante registrado con ese correo electrónico");
    }
}
}