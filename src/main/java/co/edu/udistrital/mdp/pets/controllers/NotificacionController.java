package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.NotificacionDTO;
import co.edu.udistrital.mdp.pets.dto.NotificacionDTODetail;
import co.edu.udistrital.mdp.pets.entities.NotificacionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.NotificacionService;
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final ModelMapper modelMapper;

    public NotificacionController(NotificacionService notificacionService, ModelMapper modelMapper) {
        this.notificacionService = notificacionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<NotificacionDTO> findAll() {
        return notificacionService.getNotificaciones().stream()
                .map(e -> modelMapper.map(e, NotificacionDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public NotificacionDTODetail findOne(@PathVariable Long id) throws EntityNotFoundException {
        return modelMapper.map(notificacionService.getNotificacion(id), NotificacionDTODetail.class);
    }

    @GetMapping("/canal/{canal}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<NotificacionDTO> findByCanal(@PathVariable String canal) {
        return notificacionService.getNotificacionesByCanal(canal).stream()
                .map(e -> modelMapper.map(e, NotificacionDTO.class))
                .toList();
    }

    @GetMapping("/mascota/{mascotaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<NotificacionDTO> findByMascota(@PathVariable Long mascotaId) {
        return notificacionService.getNotificacionesByMascota(mascotaId).stream()
                .map(e -> modelMapper.map(e, NotificacionDTO.class))
                .toList();
    }

    @GetMapping("/adoptante/{adoptanteId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<NotificacionDTO> findByAdoptante(@PathVariable Long adoptanteId) {
        return notificacionService.getNotificacionesByAdoptante(adoptanteId).stream()
                .map(e -> modelMapper.map(e, NotificacionDTO.class))
                .toList();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public NotificacionDTODetail create(@RequestBody NotificacionDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        NotificacionEntity entity = modelMapper.map(dto, NotificacionEntity.class);
        NotificacionEntity creada = notificacionService.createNotificacion(entity);
        return modelMapper.map(creada, NotificacionDTODetail.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public NotificacionDTODetail update(@PathVariable Long id, @RequestBody NotificacionDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        NotificacionEntity entity = modelMapper.map(dto, NotificacionEntity.class);
        NotificacionEntity actualizada = notificacionService.updateNotificacion(id, entity);
        return modelMapper.map(actualizada, NotificacionDTODetail.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        notificacionService.deleteNotificacion(id);
    }
}

