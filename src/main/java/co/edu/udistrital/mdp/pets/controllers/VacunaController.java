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

import co.edu.udistrital.mdp.pets.dto.VacunaDTO;
import co.edu.udistrital.mdp.pets.dto.VacunaDTODetail;
import co.edu.udistrital.mdp.pets.entities.VacunaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VacunaService;

/**
 * Expone VacunaService como una API REST.
 *
 * VacunaService y ModelMapper se reciben por inyeccion de dependencias
 * via constructor.
 */
@RestController
@RequestMapping("/vacunas")
public class VacunaController {

    private final VacunaService vacunaService;
    private final ModelMapper modelMapper;

    public VacunaController(VacunaService vacunaService, ModelMapper modelMapper) {
        this.vacunaService = vacunaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VacunaDTO> findAll() {
        List<VacunaEntity> vacunas = vacunaService.getVacunas();
        return vacunas.stream()
                .map(entity -> modelMapper.map(entity, VacunaDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VacunaDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        VacunaEntity vacuna = vacunaService.getVacuna(id);
        return modelMapper.map(vacuna, VacunaDTODetail.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VacunaDTODetail create(@RequestBody VacunaDTO vacunaDTO) throws IllegalOperationException {
        VacunaEntity entity = modelMapper.map(vacunaDTO, VacunaEntity.class);
        VacunaEntity creada = vacunaService.createVacuna(entity);
        return modelMapper.map(creada, VacunaDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VacunaDTODetail update(@PathVariable Long id, @RequestBody VacunaDTO vacunaDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VacunaEntity entity = modelMapper.map(vacunaDTO, VacunaEntity.class);
        VacunaEntity actualizada = vacunaService.updateVacuna(id, entity);
        return modelMapper.map(actualizada, VacunaDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        vacunaService.deleteVacuna(id);
    }
}
