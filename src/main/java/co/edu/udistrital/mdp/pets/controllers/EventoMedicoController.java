package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.EventoMedicoDTO;
import co.edu.udistrital.mdp.pets.dto.EventoMedicoDTODetail;
import co.edu.udistrital.mdp.pets.entities.EventoMedicoEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.EventoMedicoService;

@RestController
@RequestMapping("/eventos-medicos")
public class EventoMedicoController {

    private final EventoMedicoService eventoMedicoService;
    private final ModelMapper modelMapper;

    public EventoMedicoController(EventoMedicoService eventoMedicoService, ModelMapper modelMapper) {
        this.eventoMedicoService = eventoMedicoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<EventoMedicoDTO> findAll() {
        return eventoMedicoService.getEventosMedicos().stream()
                .map(e -> modelMapper.map(e, EventoMedicoDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventoMedicoDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        return modelMapper.map(eventoMedicoService.getEventoMedico(id), EventoMedicoDTODetail.class);
    }

    @GetMapping("/mascota/{mascotaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<EventoMedicoDTO> findByMascota(@PathVariable Long mascotaId) {
        return eventoMedicoService.getEventosMedicosByMascota(mascotaId).stream()
                .map(e -> modelMapper.map(e, EventoMedicoDTO.class))
                .toList();
    }

    @GetMapping("/diagnostico/{diagnostico}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<EventoMedicoDTO> findByDiagnostico(@PathVariable String diagnostico) {
        return eventoMedicoService.getEventosMedicosByDiagnostico(diagnostico).stream()
                .map(e -> modelMapper.map(e, EventoMedicoDTO.class))
                .toList();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventoMedicoDTODetail create(@RequestBody EventoMedicoDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        EventoMedicoEntity entity = modelMapper.map(dto, EventoMedicoEntity.class);
        EventoMedicoEntity creado = eventoMedicoService.createEventoMedico(entity);
        return modelMapper.map(creado, EventoMedicoDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventoMedicoDTODetail update(@PathVariable Long id, @RequestBody EventoMedicoDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        EventoMedicoEntity entity = modelMapper.map(dto, EventoMedicoEntity.class);
        EventoMedicoEntity actualizado = eventoMedicoService.updateEventoMedico(id, entity);
        return modelMapper.map(actualizado, EventoMedicoDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        eventoMedicoService.deleteEventoMedico(id);
    }
}