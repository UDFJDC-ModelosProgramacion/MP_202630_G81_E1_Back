package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.SeguimientoDTO;
import co.edu.udistrital.mdp.pets.entities.SeguimientoEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.SeguimientoService;

/**
 * Expone SeguimientoService como una API REST.
 *
 * Para crear/actualizar un seguimiento, el cliente debe enviar en el
 * cuerpo: "mascota": { "id": &lt;idMascota&gt; } y
 * "veterinario": { "id": &lt;idVeterinario&gt; }.
 *
 * SeguimientoService y ModelMapper se reciben por inyeccion de
 * dependencias via constructor.
 */
@RestController
@RequestMapping("/seguimientos")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;
    private final ModelMapper modelMapper;

    public SeguimientoController(SeguimientoService seguimientoService, ModelMapper modelMapper) {
        this.seguimientoService = seguimientoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<SeguimientoDTO> findAll() {
        List<SeguimientoEntity> seguimientos = seguimientoService.getSeguimientos();
        return seguimientos.stream()
                .map(entity -> modelMapper.map(entity, SeguimientoDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public SeguimientoDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        SeguimientoEntity seguimiento = seguimientoService.getSeguimiento(id);
        return modelMapper.map(seguimiento, SeguimientoDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SeguimientoDTO create(@RequestBody SeguimientoDTO seguimientoDTO)
            throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity entity = modelMapper.map(seguimientoDTO, SeguimientoEntity.class);
        SeguimientoEntity creado = seguimientoService.createSeguimiento(entity);
        return modelMapper.map(creado, SeguimientoDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public SeguimientoDTO update(@PathVariable Long id, @RequestBody SeguimientoDTO seguimientoDTO)
            throws EntityNotFoundException, IllegalOperationException {
        SeguimientoEntity entity = modelMapper.map(seguimientoDTO, SeguimientoEntity.class);
        SeguimientoEntity actualizado = seguimientoService.updateSeguimiento(id, entity);
        return modelMapper.map(actualizado, SeguimientoDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        seguimientoService.deleteSeguimiento(id);
    }
}
