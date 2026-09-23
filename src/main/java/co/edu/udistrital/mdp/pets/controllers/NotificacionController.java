package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.NotificacionEntity;
import co.edu.udistrital.mdp.pets.services.NotificacionService;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionEntity> getNotificaciones() {
        return notificacionService.getNotificaciones();
    }

    @GetMapping("/{id}")
    public NotificacionEntity getNotificacion(@PathVariable Long id) {
        return notificacionService.getNotificacion(id);
    }

    @GetMapping("/canal/{canal}")
    public List<NotificacionEntity> getNotificacionesByCanal(@PathVariable String canal) {
        return notificacionService.getNotificacionesByCanal(canal);
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<NotificacionEntity> getNotificacionesByMascota(@PathVariable Long mascotaId) {
        return notificacionService.getNotificacionesByMascota(mascotaId);
    }

    @GetMapping("/adoptante/{adoptanteId}")
    public List<NotificacionEntity> getNotificacionesByAdoptante(@PathVariable Long adoptanteId) {
        return notificacionService.getNotificacionesByAdoptante(adoptanteId);
    }

    @PostMapping
    public NotificacionEntity createNotificacion(@RequestBody NotificacionEntity notificacion) {
        return notificacionService.createNotificacion(notificacion);
    }

    @PutMapping("/{id}")
    public NotificacionEntity updateNotificacion(@PathVariable Long id,
            @RequestBody NotificacionEntity notificacion) {
        return notificacionService.updateNotificacion(id, notificacion);
    }

    @DeleteMapping("/{id}")
    public void deleteNotificacion(@PathVariable Long id) {
        notificacionService.deleteNotificacion(id);
    }
}