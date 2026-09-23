package co.edu.udistrital.mdp.pets.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
 * Clase que implementa la lógica de negocio para la entidad RegistroVacunacion.
 */
@Service
@RequiredArgsConstructor
public class RegistroVacunacionService {

    private final RegistroVacunacionRepository registroVacunacionRepository;
    private final VacunaRepository vacunaRepository;
    private final MascotaRepository mascotaRepository;

    @Transactional
    public RegistroVacunacionEntity createRegistroVacunacion(RegistroVacunacionEntity registro) 
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
	public RegistroVacunacionEntity getRegistroVacunacion(Long id) throws EntityNotFoundException {
		Optional<RegistroVacunacionEntity> registro = registroVacunacionRepository.findById(id);
		if (registro.isEmpty()) {
			throw new EntityNotFoundException("El registro de vacunación con el id dado no fue encontrado");
		}
		return registro.get();
	}

	@Transactional
	public RegistroVacunacionEntity updateRegistroVacunacion(Long id, RegistroVacunacionEntity registro)
			throws EntityNotFoundException, IllegalOperationException {
		Optional<RegistroVacunacionEntity> existente = registroVacunacionRepository.findById(id);
		if (existente.isEmpty()) {
			throw new EntityNotFoundException("El registro de vacunación con el id dado no fue encontrado");
		}
		validarDatosBasicos(registro);
		validarVacunaYMascota(registro);
		registro.setId(id);
		return registroVacunacionRepository.save(registro);
	}

	@Transactional
	public void deleteRegistroVacunacion(Long id) throws EntityNotFoundException {
		Optional<RegistroVacunacionEntity> registro = registroVacunacionRepository.findById(id);
		if (registro.isEmpty()) {
			throw new EntityNotFoundException("El registro de vacunación con el id dado no fue encontrado");
		}
		registroVacunacionRepository.deleteById(id);
	}

	private void validarDatosBasicos(RegistroVacunacionEntity registro) throws IllegalOperationException {
		if (registro.getFechaAplicacion() == null) {
			throw new IllegalOperationException("La fecha de aplicación es obligatoria");
		}
		if (registro.getFechaAplicacion().after(new Date())) {
			throw new IllegalOperationException("La fecha de aplicación no puede ser futura");
		}
		if (registro.getProximaFecha() != null
				&& !registro.getProximaFecha().after(registro.getFechaAplicacion())) {
			throw new IllegalOperationException("La próxima fecha debe ser posterior a la fecha de aplicación");
		}
		if (registro.getNumeroLote() == null || registro.getNumeroLote().isBlank()) {
			throw new IllegalOperationException("El número de lote es obligatorio");
		}
	}

	private void validarVacunaYMascota(RegistroVacunacionEntity registro)
			throws IllegalOperationException, EntityNotFoundException {
		if (registro.getVacuna() == null || registro.getVacuna().getId() == null) {
			throw new IllegalOperationException("El registro debe estar asociado a una vacuna");
		}
		Optional<VacunaEntity> vacuna = vacunaRepository.findById(registro.getVacuna().getId());
		if (vacuna.isEmpty()) {
			throw new EntityNotFoundException("La vacuna asociada al registro no existe");
		}

		if (registro.getMascota() == null || registro.getMascota().getId() == null) {
			throw new IllegalOperationException("El registro debe estar asociado a una mascota");
		}
		Optional<MascotaEntity> mascota = mascotaRepository.findById(registro.getMascota().getId());
		if (mascota.isEmpty()) {
			throw new EntityNotFoundException("La mascota asociada al registro no existe");
		}
	}
}
