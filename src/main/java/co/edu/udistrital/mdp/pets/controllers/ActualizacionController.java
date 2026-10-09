package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ActualizacionDTO;
import co.edu.udistrital.mdp.pets.dto.ActualizacionDTODetail;
import co.edu.udistrital.mdp.pets.entities.ActualizacionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ActualizacionService;

@RestController
@RequestMapping("/actualizaciones")
public class ActualizacionController {

    private final ActualizacionService actualizacionService;
    private final ModelMapper modelMapper;

    public ActualizacionController(ActualizacionService actualizacionService, ModelMapper modelMapper) {
        this.actualizacionService = actualizacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ActualizacionDTO> findAll() {
        return actualizacionService.getActualizaciones().stream()
                .map(e -> modelMapper.map(e, ActualizacionDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ActualizacionDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        return modelMapper.map(actualizacionService.getActualizacion(id), ActualizacionDTODetail.class);
    }

    @GetMapping("/tipo/{tipo}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ActualizacionDTO> findByTipo(@PathVariable String tipo) {
        return actualizacionService.getActualizacionesByTipo(tipo).stream()
                .map(e -> modelMapper.map(e, ActualizacionDTO.class))
                .toList();
    }

    @GetMapping("/mascota/{mascotaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ActualizacionDTO> findByMascota(@PathVariable Long mascotaId) {
        return actualizacionService.getActualizacionesByMascota(mascotaId).stream()
                .map(e -> modelMapper.map(e, ActualizacionDTO.class))
                .toList();
    }

    @GetMapping("/adoptante/{adoptanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ActualizacionDTO> findByAdoptante(@PathVariable Long adoptanteId) {
        return actualizacionService.getActualizacionesByAdoptante(adoptanteId).stream()
                .map(e -> modelMapper.map(e, ActualizacionDTO.class))
                .toList();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ActualizacionDTODetail create(@RequestBody ActualizacionDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        ActualizacionEntity entity = modelMapper.map(dto, ActualizacionEntity.class);
        ActualizacionEntity creada = actualizacionService.createActualizacion(entity);
        return modelMapper.map(creada, ActualizacionDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ActualizacionDTODetail update(@PathVariable Long id, @RequestBody ActualizacionDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        ActualizacionEntity entity = modelMapper.map(dto, ActualizacionEntity.class);
        ActualizacionEntity actualizada = actualizacionService.updateActualizacion(id, entity);
        return modelMapper.map(actualizada, ActualizacionDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        actualizacionService.deleteActualizacion(id);
    }
}