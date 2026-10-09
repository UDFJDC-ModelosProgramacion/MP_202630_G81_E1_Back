package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ResenaDTO;
import co.edu.udistrital.mdp.pets.dto.ResenaDTODetail;
import co.edu.udistrital.mdp.pets.entities.ResenaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ResenaService;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;
    private final ModelMapper modelMapper;

    public ResenaController(ResenaService resenaService, ModelMapper modelMapper) {
        this.resenaService = resenaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResenaDTO> findAll() {
        return resenaService.getResenas().stream()
                .map(e -> modelMapper.map(e, ResenaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResenaDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        return modelMapper.map(resenaService.getResena(id), ResenaDTODetail.class);
    }

    @GetMapping("/mascota/{mascotaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResenaDTO> findByMascota(@PathVariable Long mascotaId) {
        return resenaService.getResenasByMascota(mascotaId).stream()
                .map(e -> modelMapper.map(e, ResenaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/adoptante/{adoptanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResenaDTO> findByAdoptante(@PathVariable Long adoptanteId) {
        return resenaService.getResenasByAdoptante(adoptanteId).stream()
                .map(e -> modelMapper.map(e, ResenaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/calificacion/{calificacion}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResenaDTO> findByCalificacion(@PathVariable Integer calificacion) {
        return resenaService.getResenasByCalificacion(calificacion).stream()
                .map(e -> modelMapper.map(e, ResenaDTO.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResenaDTODetail create(@RequestBody ResenaDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        ResenaEntity entity = modelMapper.map(dto, ResenaEntity.class);
        ResenaEntity creada = resenaService.createResena(entity);
        return modelMapper.map(creada, ResenaDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResenaDTODetail update(@PathVariable Long id, @RequestBody ResenaDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        ResenaEntity entity = modelMapper.map(dto, ResenaEntity.class);
        ResenaEntity actualizada = resenaService.updateResena(id, entity);
        return modelMapper.map(actualizada, ResenaDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        resenaService.deleteResena(id);
    }
}