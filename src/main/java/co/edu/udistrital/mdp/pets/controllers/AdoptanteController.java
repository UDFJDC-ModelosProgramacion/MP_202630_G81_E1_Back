package co.edu.udistrital.mdp.pets.controllers;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdoptanteDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptanteDTODetail;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptanteService;

/**
 * Expone AdoptanteService como una API REST.
 *
 * AdoptanteService y ModelMapper se reciben por inyeccion de dependencias
 * via constructor.
 */
@RestController
@RequestMapping("/adoptantes")
public class AdoptanteController {

    private final AdoptanteService adoptanteService;
    private final ModelMapper modelMapper;

    public AdoptanteController(AdoptanteService adoptanteService, ModelMapper modelMapper) {
        this.adoptanteService = adoptanteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptanteDTO> findAll() {
        List<AdoptanteEntity> adoptantes = adoptanteService.getAdoptantes();
        return adoptantes.stream()
                .map(entity -> modelMapper.map(entity, AdoptanteDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptanteDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptanteEntity adoptante = adoptanteService.getAdoptante(id);
        return modelMapper.map(adoptante, AdoptanteDTODetail.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptanteDTODetail create(@RequestBody AdoptanteDTO adoptanteDTO) throws IllegalOperationException {
        AdoptanteEntity entity = modelMapper.map(adoptanteDTO, AdoptanteEntity.class);
        AdoptanteEntity creado = adoptanteService.createAdoptante(entity);
        return modelMapper.map(creado, AdoptanteDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptanteDTODetail update(@PathVariable Long id, @RequestBody AdoptanteDTO adoptanteDTO)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptanteEntity entity = modelMapper.map(adoptanteDTO, AdoptanteEntity.class);
        AdoptanteEntity actualizado = adoptanteService.updateAdoptante(id, entity);
        return modelMapper.map(actualizado, AdoptanteDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        adoptanteService.deleteAdoptante(id);
    }
}
