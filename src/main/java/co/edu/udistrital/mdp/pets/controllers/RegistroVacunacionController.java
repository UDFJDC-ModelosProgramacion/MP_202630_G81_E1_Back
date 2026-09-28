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

import co.edu.udistrital.mdp.pets.dto.RegistroVacunacionDTO;
import co.edu.udistrital.mdp.pets.dto.RegistroVacunacionDTODetail;
import co.edu.udistrital.mdp.pets.entities.RegistroVacunacionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.RegistroVacunacionService;

/**
 * Expone RegistroVacunacionService como una API REST.
 *
 * Para asociar un registro a su vacuna y su mascota, el cliente debe
 * enviar en el cuerpo del POST/PUT: "vacuna": { "id": &lt;idVacuna&gt; } y
 * "mascota": { "id": &lt;idMascota&gt; }. "seguimientoOrigen" es opcional.
 * El resto de la validacion de esas asociaciones la hace
 * RegistroVacunacionService.
 *
 * RegistroVacunacionService y ModelMapper se reciben por inyeccion de
 * dependencias via constructor.
 */
@RestController
@RequestMapping("/registros-vacunacion")
public class RegistroVacunacionController {

    private final RegistroVacunacionService registroVacunacionService;
    private final ModelMapper modelMapper;

    public RegistroVacunacionController(RegistroVacunacionService registroVacunacionService,
            ModelMapper modelMapper) {
        this.registroVacunacionService = registroVacunacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RegistroVacunacionDTO> findAll() {
        List<RegistroVacunacionEntity> registros = registroVacunacionService.getRegistrosVacunacion();
        return registros.stream()
                .map(entity -> modelMapper.map(entity, RegistroVacunacionDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RegistroVacunacionDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        RegistroVacunacionEntity registro = registroVacunacionService.getRegistroVacunacion(id);
        return modelMapper.map(registro, RegistroVacunacionDTODetail.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RegistroVacunacionDTODetail create(@RequestBody RegistroVacunacionDTO registroDTO)
            throws EntityNotFoundException, IllegalOperationException {
        RegistroVacunacionEntity entity = modelMapper.map(registroDTO, RegistroVacunacionEntity.class);
        RegistroVacunacionEntity creado = registroVacunacionService.createRegistroVacunacion(entity);
        return modelMapper.map(creado, RegistroVacunacionDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RegistroVacunacionDTODetail update(@PathVariable Long id,
            @RequestBody RegistroVacunacionDTO registroDTO)
            throws EntityNotFoundException, IllegalOperationException {
        RegistroVacunacionEntity entity = modelMapper.map(registroDTO, RegistroVacunacionEntity.class);
        RegistroVacunacionEntity actualizado = registroVacunacionService.updateRegistroVacunacion(id, entity);
        return modelMapper.map(actualizado, RegistroVacunacionDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        registroVacunacionService.deleteRegistroVacunacion(id);
    }
}
