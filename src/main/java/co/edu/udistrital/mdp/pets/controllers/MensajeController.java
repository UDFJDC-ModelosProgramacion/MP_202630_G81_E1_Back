package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.MensajeDTO;
import co.edu.udistrital.mdp.pets.dto.MensajeDTODetail;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.MensajeEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.MensajeRepository;

@RestController
@RequestMapping("/adoptantes/{adoptanteId}/mensajes")
public class MensajeController {

    private final MensajeRepository mensajeRepository;
    private final AdoptanteRepository adoptanteRepository;
    private final MascotaRepository mascotaRepository;
    private final ModelMapper modelMapper;

    public MensajeController(MensajeRepository mensajeRepository, AdoptanteRepository adoptanteRepository,
            MascotaRepository mascotaRepository, ModelMapper modelMapper) {
        this.mensajeRepository = mensajeRepository;
        this.adoptanteRepository = adoptanteRepository;
        this.mascotaRepository = mascotaRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<MensajeDTODetail> findAll(@PathVariable Long adoptanteId) throws EntityNotFoundException {
        requireAdoptante(adoptanteId);
        return mensajeRepository.findByAdoptanteId(adoptanteId).stream()
                .map(entity -> modelMapper.map(entity, MensajeDTODetail.class)).toList();
    }

    @GetMapping("/{mensajeId}")
    public MensajeDTODetail findOne(@PathVariable Long adoptanteId, @PathVariable Long mensajeId)
            throws EntityNotFoundException {
        return detail(requireMensaje(adoptanteId, mensajeId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MensajeDTODetail create(@PathVariable Long adoptanteId, @RequestBody MensajeDTO dto)
            throws EntityNotFoundException {
        AdoptanteEntity adoptante = requireAdoptante(adoptanteId);
        MensajeEntity entity = modelMapper.map(dto, MensajeEntity.class);
        entity.setAdoptante(adoptante);
        if (dto.getMascotaId() != null) {
            entity.setMascota(requireMascota(dto.getMascotaId()));
        }
        return detail(mensajeRepository.save(entity));
    }

    @PutMapping("/{mensajeId}")
    public MensajeDTODetail update(@PathVariable Long adoptanteId, @PathVariable Long mensajeId,
            @RequestBody MensajeDTO dto) throws EntityNotFoundException, IllegalOperationException {
        MensajeEntity entity = requireMensaje(adoptanteId, mensajeId);
        if (entity.isLeido()) {
            throw new IllegalOperationException("El mensaje leído no puede ser editado");
        }
        entity.setFecha(dto.getFecha());
        entity.setAsunto(dto.getAsunto());
        entity.setContenido(dto.getContenido());
        entity.setLeido(dto.isLeido());
        entity.setAdoptante(requireAdoptante(adoptanteId));
        if (dto.getMascotaId() != null) {
            entity.setMascota(requireMascota(dto.getMascotaId()));
        }
        return detail(mensajeRepository.save(entity));
    }

    @PatchMapping("/{mensajeId}/leido")
    public MensajeDTODetail markRead(@PathVariable Long adoptanteId, @PathVariable Long mensajeId)
            throws EntityNotFoundException {
        MensajeEntity entity = requireMensaje(adoptanteId, mensajeId);
        entity.setLeido(true);
        return detail(mensajeRepository.save(entity));
    }

    @PatchMapping("/{mensajeId}/no-leido")
    public MensajeDTODetail markUnread(@PathVariable Long adoptanteId, @PathVariable Long mensajeId)
            throws EntityNotFoundException {
        MensajeEntity entity = requireMensaje(adoptanteId, mensajeId);
        entity.setLeido(false);
        return detail(mensajeRepository.save(entity));
    }

    @DeleteMapping("/{mensajeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long adoptanteId, @PathVariable Long mensajeId)
            throws EntityNotFoundException, IllegalOperationException {
        MensajeEntity entity = requireMensaje(adoptanteId, mensajeId);
        if (entity.isLeido()) {
            throw new IllegalOperationException("El mensaje leído no puede ser eliminado");
        }
        mensajeRepository.deleteById(mensajeId);
    }

    private AdoptanteEntity requireAdoptante(Long id) throws EntityNotFoundException {
        return adoptanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El adoptante con el id dado no existe"));
    }

    private MascotaEntity requireMascota(Long id) throws EntityNotFoundException {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La mascota con el id dado no existe"));
    }

    private MensajeEntity requireMensaje(Long adoptanteId, Long mensajeId) throws EntityNotFoundException {
        requireAdoptante(adoptanteId);
        MensajeEntity entity = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new EntityNotFoundException("El mensaje con el id dado no existe"));
        if (entity.getAdoptante() == null || !adoptanteId.equals(entity.getAdoptante().getId())) {
            throw new EntityNotFoundException("El mensaje con el id dado no existe para este adoptante");
        }
        return entity;
    }

    private MensajeDTODetail detail(MensajeEntity entity) {
        return modelMapper.map(entity, MensajeDTODetail.class);
    }
}
