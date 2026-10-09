package co.edu.udistrital.mdp.pets.controllers;

import java.sql.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.EventoVidaDTO;
import co.edu.udistrital.mdp.pets.dto.EventoVidaDTODetail;
import co.edu.udistrital.mdp.pets.entities.EventoVidaEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.EventoVidaRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@RestController
@RequestMapping("/mascotas/{mascotaId}/eventos-vida")
public class EventoVidaController {

    private final EventoVidaRepository eventoVidaRepository;
    private final MascotaRepository mascotaRepository;
    private final ModelMapper modelMapper;

    public EventoVidaController(EventoVidaRepository eventoVidaRepository,
            MascotaRepository mascotaRepository, ModelMapper modelMapper) {
        this.eventoVidaRepository = eventoVidaRepository;
        this.mascotaRepository = mascotaRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventoVidaDTODetail> findAll(@PathVariable Long mascotaId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Date fechaInicio,
            @RequestParam(required = false) Date fechaFin) throws EntityNotFoundException {
        requireMascota(mascotaId);
        if (tipo == null) {
            return details(eventoVidaRepository.findByMascotaId(mascotaId));
        }
        if (fechaInicio != null && fechaFin != null) {
            return details(eventoVidaRepository.findByMascotaIdAndTipoAndFechaBetween(
                    mascotaId, tipo, fechaInicio, fechaFin));
        }
        if (fechaInicio != null) {
            return details(eventoVidaRepository.findByMascotaIdAndTipoAndFechaAfter(
                    mascotaId, tipo, fechaInicio));
        }
        return details(eventoVidaRepository.findByMascotaIdAndTipo(mascotaId, tipo));
    }

    @GetMapping("/{eventoVidaId}")
    @ResponseStatus(HttpStatus.OK)
    public EventoVidaDTODetail findOne(@PathVariable Long mascotaId, @PathVariable Long eventoVidaId)
            throws EntityNotFoundException {
        requireMascota(mascotaId);
        return detail(eventoVidaRepository.findByMascotaIdAndId(mascotaId, eventoVidaId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoVidaDTODetail create(@PathVariable Long mascotaId, @RequestBody EventoVidaDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity mascota = requireMascota(mascotaId);
        EventoVidaEntity entity = modelMapper.map(dto, EventoVidaEntity.class);
        entity.setMascota(mascota);
        validate(entity);
        return detail(eventoVidaRepository.save(entity));
    }

    @PutMapping("/{eventoVidaId}")
    @ResponseStatus(HttpStatus.OK)
    public EventoVidaDTODetail update(@PathVariable Long mascotaId, @PathVariable Long eventoVidaId,
            @RequestBody EventoVidaDTO dto) throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity mascota = requireMascota(mascotaId);
        EventoVidaEntity entity = requireEvent(mascotaId, eventoVidaId);
        entity.setTipo(dto.getTipo());
        entity.setFecha(dto.getFecha());
        entity.setDescripcion(dto.getDescripcion());
        entity.setMascota(mascota);
        validate(entity);
        return detail(eventoVidaRepository.save(entity));
    }

    @DeleteMapping("/{eventoVidaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long mascotaId, @PathVariable Long eventoVidaId) {
        requireEvent(mascotaId, eventoVidaId);
        eventoVidaRepository.deleteById(eventoVidaId);
    }

    private MascotaEntity requireMascota(Long id) throws EntityNotFoundException {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La mascota con el id dado no existe"));
    }

    private EventoVidaEntity requireEvent(Long mascotaId, Long id) {
        return eventoVidaRepository.findByMascotaIdAndId(mascotaId, id);
    }

    private void validate(EventoVidaEntity entity) throws IllegalOperationException {
        if (entity.getTipo() == null || entity.getTipo().isBlank() || entity.getFecha() == null
                || !entity.getFecha().before(new Date(System.currentTimeMillis()))) {
            throw new IllegalOperationException("El tipo y la fecha pasada son obligatorios");
        }
    }

    private EventoVidaDTODetail detail(EventoVidaEntity entity) throws EntityNotFoundException {
        if (entity == null) {
            throw new EntityNotFoundException("El evento de vida con el id dado no existe");
        }
        return modelMapper.map(entity, EventoVidaDTODetail.class);
    }

    private List<EventoVidaDTODetail> details(List<EventoVidaEntity> entities) {
        return entities.stream().map(entity -> modelMapper.map(entity, EventoVidaDTODetail.class))
                .toList();
    }
}
