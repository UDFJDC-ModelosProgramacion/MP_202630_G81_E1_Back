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

import co.edu.udistrital.mdp.pets.dto.MascotaDTO;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MascotaService;

/**
 * Expone MascotaService como una API REST.
 *
 * Para asociar una mascota a un refugio, el cliente debe enviar en el
 * cuerpo del POST/PUT: "refugio": { "id": &lt;idDelRefugio&gt; }. El resto
 * de la validacion de esa asociacion la hace MascotaService.
 *
 * MascotaService y ModelMapper se reciben por inyeccion de dependencias
 * via constructor.
 */
@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final ModelMapper modelMapper;

    @Autowired
    public MascotaController(MascotaService mascotaService, ModelMapper modelMapper) {
        this.mascotaService = mascotaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MascotaDTO> findAll() {
        List<MascotaEntity> mascotas = mascotaService.getMascotas();
        return mascotas.stream()
                .map(entity -> modelMapper.map(entity, MascotaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MascotaDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MascotaEntity mascota = mascotaService.getMascota(id);
        return modelMapper.map(mascota, MascotaDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MascotaDTO create(@RequestBody MascotaDTO mascotaDTO)
            throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity entity = modelMapper.map(mascotaDTO, MascotaEntity.class);
        MascotaEntity creada = mascotaService.createMascota(entity);
        return modelMapper.map(creada, MascotaDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MascotaDTO update(@PathVariable Long id, @RequestBody MascotaDTO mascotaDTO)
            throws EntityNotFoundException, IllegalOperationException {
        MascotaEntity entity = modelMapper.map(mascotaDTO, MascotaEntity.class);
        MascotaEntity actualizada = mascotaService.updateMascota(id, entity);
        return modelMapper.map(actualizada, MascotaDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        mascotaService.deleteMascota(id);
    }
}
