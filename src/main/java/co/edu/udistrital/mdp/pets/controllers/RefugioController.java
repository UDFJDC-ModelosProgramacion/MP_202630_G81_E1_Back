package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import co.edu.udistrital.mdp.pets.dto.RefugioDTO;
import co.edu.udistrital.mdp.pets.entities.RefugioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.RefugioService;

/**
 * Expone RefugioService como una API REST siguiendo las buenas practicas
 * vistas en clase: sustantivo en plural, verbos HTTP para las operaciones,
 * y codigos de estado especificos (201 al crear, 204 al borrar).
 *
 * RefugioService y ModelMapper se reciben por inyeccion de dependencias
 * via constructor.
 */
@RestController
@RequestMapping("/refugios")
public class RefugioController {

    private final RefugioService refugioService;
    private final ModelMapper modelMapper;

    @Autowired
    public RefugioController(RefugioService refugioService, ModelMapper modelMapper) {
        this.refugioService = refugioService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RefugioDTO> findAll() {
        List<RefugioEntity> refugios = refugioService.getRefugios();
        return refugios.stream()
                .map(entity -> modelMapper.map(entity, RefugioDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RefugioDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        RefugioEntity refugio = refugioService.getRefugio(id);
        return modelMapper.map(refugio, RefugioDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RefugioDTO create(@RequestBody RefugioDTO refugioDTO) throws IllegalOperationException {
        RefugioEntity entity = modelMapper.map(refugioDTO, RefugioEntity.class);
        RefugioEntity creado = refugioService.createRefugio(entity);
        return modelMapper.map(creado, RefugioDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RefugioDTO update(@PathVariable Long id, @RequestBody RefugioDTO refugioDTO)
            throws EntityNotFoundException, IllegalOperationException {
        RefugioEntity entity = modelMapper.map(refugioDTO, RefugioEntity.class);
        RefugioEntity actualizado = refugioService.updateRefugio(id, entity);
        return modelMapper.map(actualizado, RefugioDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        refugioService.deleteRefugio(id);
    }
}
