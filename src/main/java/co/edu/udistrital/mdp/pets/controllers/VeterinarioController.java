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

import co.edu.udistrital.mdp.pets.dto.VeterinarioDTO;
import co.edu.udistrital.mdp.pets.entities.VeterinarioEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VeterinarioService;

/**
 * Expone VeterinarioService como una API REST.
 *
 * Para asociar un veterinario a un refugio, el cliente debe enviar en el
 * cuerpo del POST/PUT: "refugio": { "id": &lt;idDelRefugio&gt; }.
 *
 * VeterinarioService y ModelMapper se reciben por inyeccion de
 * dependencias via constructor.
 */
@RestController
@RequestMapping("/veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;
    private final ModelMapper modelMapper;

    @Autowired
    public VeterinarioController(VeterinarioService veterinarioService, ModelMapper modelMapper) {
        this.veterinarioService = veterinarioService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VeterinarioDTO> findAll() {
        List<VeterinarioEntity> veterinarios = veterinarioService.getVeterinarios();
        return veterinarios.stream()
                .map(entity -> modelMapper.map(entity, VeterinarioDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VeterinarioDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        VeterinarioEntity veterinario = veterinarioService.getVeterinario(id);
        return modelMapper.map(veterinario, VeterinarioDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VeterinarioDTO create(@RequestBody VeterinarioDTO veterinarioDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarioEntity entity = modelMapper.map(veterinarioDTO, VeterinarioEntity.class);
        VeterinarioEntity creado = veterinarioService.createVeterinario(entity);
        return modelMapper.map(creado, VeterinarioDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VeterinarioDTO update(@PathVariable Long id, @RequestBody VeterinarioDTO veterinarioDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarioEntity entity = modelMapper.map(veterinarioDTO, VeterinarioEntity.class);
        VeterinarioEntity actualizado = veterinarioService.updateVeterinario(id, entity);
        return modelMapper.map(actualizado, VeterinarioDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        veterinarioService.deleteVeterinario(id);
    }
}
