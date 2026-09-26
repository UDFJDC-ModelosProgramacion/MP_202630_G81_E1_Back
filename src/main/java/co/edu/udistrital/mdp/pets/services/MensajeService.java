package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.MensajeEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.MensajeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j 
@Service
public class MensajeService {
    private final MascotaRepository mascotaRepository;
    private final AdoptanteRepository adoptanteRepository;
	private final MensajeRepository mensajeRepository;

	public MensajeService(MensajeRepository mensajeRepository, MascotaRepository mascotaRepository, AdoptanteRepository adoptanteRepository) {
		this.mascotaRepository = mascotaRepository;
        this.adoptanteRepository = adoptanteRepository;
        this.mensajeRepository = mensajeRepository;
	}

    @Transactional(rollbackOn = Exception.class)
	public List<MensajeEntity> getAllMensajes(Long idAdoptante) {
        try {
            if(adoptanteRepository.existsById(idAdoptante)) {
                log.info("El adoptante con ID: {} existe", idAdoptante);
            } else {
                log.warn("El adoptante con ID: {} no existe", idAdoptante);
                throw new IllegalArgumentException("El adoptante con ID: " + idAdoptante + " no existe");
            }

            log.info("Listando todos los mensajes");
            return mensajeRepository.findByAdoptanteId(idAdoptante);
        } catch (Exception e) {
            log.error("Error al listar los mensajes", e);
            throw new RuntimeException("Error al listar los mensajes", e);
        }
		
	}

    @Transactional(rollbackOn = Exception.class)
    public List<MensajeEntity> getMensajesByMascotaId(Long idMascota) {
        try {
            if(mascotaRepository.existsById(idMascota)) {
                log.info("La mascota con ID: {} existe", idMascota);
            } else {
                log.warn("La mascota con ID: {} no existe", idMascota);
                throw new IllegalArgumentException("La mascota con ID: " + idMascota + " no existe");
            }

            log.info("Listando todos los mensajes de la mascota con ID: {}", idMascota);
            return mensajeRepository.findByMascotaId(idMascota);
        } catch (Exception e) {
            log.error("Error al listar los mensajes de la mascota con ID: {}", idMascota, e);
            throw new RuntimeException("Error al listar los mensajes de la mascota con ID: " + idMascota, e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void eliminarMensaje(Long idMensaje, Long idAdoptante) {
        try {
            
            if(!mensajeRepository.existsById(idMensaje)) {
                log.warn("El mensaje con ID: {} no existe", idMensaje);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " no existe");
            }

            if(mensajeRepository.findById(idMensaje).get().getAdoptante() != adoptanteRepository.findById(idAdoptante).get()) {
                log.warn("El mensaje con ID: {} no pertenece al adoptante con ID: {}", idMensaje, idAdoptante);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " no pertenece al adoptante con ID: " + idAdoptante);
            }

            if(mensajeRepository.findById(idMensaje).get().isLeido()) {
                log.warn("El mensaje con ID: {} ya ha sido leído y no puede ser eliminado", idMensaje);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " ya ha sido leído y no puede ser eliminado");
            }

            log.info("Eliminando mensaje con ID: {}", idMensaje);
            mensajeRepository.deleteById(idMensaje);
        } catch (Exception e) {
            log.error("Error al eliminar el mensaje con ID: {}", idMensaje, e);
            throw new RuntimeException("Error al eliminar el mensaje con ID: " + idMensaje, e);
        }
    }

    @Transactional (rollbackOn = Exception.class)
    public void marcarMensajeComoLeido(Long idMensaje, Long idAdoptante) {
        try {
            if(!mensajeRepository.existsById(idMensaje)) {
                log.warn("El mensaje con ID: {} no existe", idMensaje);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " no existe");
            }

            if(mensajeRepository.findById(idMensaje).get().getAdoptante() != adoptanteRepository.findById(idAdoptante).get()) {
                log.warn("El mensaje con ID: {} no pertenece al adoptante con ID: {}", idMensaje, idAdoptante);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " no pertenece al adoptante con ID: " + idAdoptante);
            }

            MensajeEntity mensaje = mensajeRepository.findById(idMensaje).get();
            mensaje.setLeido(true);
            mensajeRepository.save(mensaje);

            log.info("Marcando mensaje con ID: {} como leído", idMensaje);
        } catch (Exception e) {
            log.error("Error al marcar el mensaje con ID: {} como leído", idMensaje, e);
            throw new RuntimeException("Error al marcar el mensaje con ID: " + idMensaje + " como leído", e);
        }
    }

    @Transactional (rollbackOn = Exception.class)
    public void marcarMensajeComoNoLeido(Long idMensaje, Long idAdoptante) {
        try {
            if(!mensajeRepository.existsById(idMensaje)) {
                log.warn("El mensaje con ID: {} no existe", idMensaje);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " no existe");
            }

            if(mensajeRepository.findById(idMensaje).get().getAdoptante() != adoptanteRepository.findById(idAdoptante).get()) {
                log.warn("El mensaje con ID: {} no pertenece al adoptante con ID: {}", idMensaje, idAdoptante);
                throw new IllegalArgumentException("El mensaje con ID: " + idMensaje + " no pertenece al adoptante con ID: " + idAdoptante);
            }

            MensajeEntity mensaje = mensajeRepository.findById(idMensaje).get();
            mensaje.setLeido(false);
            mensajeRepository.save(mensaje);

            log.info("Marcando mensaje con ID: {} como no leído", idMensaje);
        } catch (Exception e) {
            log.error("Error al marcar el mensaje con ID: {} como no leído", idMensaje, e);
            throw new RuntimeException("Error al marcar el mensaje con ID: " + idMensaje + " como no leído", e);
        }
    }

    @Transactional (rollbackOn = Exception.class)
    public void editarMensaje(MensajeEntity mensaje, Long idAdoptante) {
        try {
            if(!mensajeRepository.existsById(mensaje.getId())) {
                log.warn("El mensaje con ID: {} no existe", mensaje.getId());
                throw new IllegalArgumentException("El mensaje con ID: " + mensaje.getId() + " no existe");
            }

            if(!adoptanteRepository.existsById(idAdoptante)) {
                log.warn("El adoptante con ID: {} no existe", idAdoptante);
                throw new IllegalArgumentException("El adoptante con ID: " + idAdoptante + " no existe");
            }

            if(mensaje.isLeido()) {
                log.warn("El mensaje con ID: {} ya ha sido leído y no puede ser editado", mensaje.getId());
                throw new IllegalArgumentException("El mensaje con ID: " + mensaje.getId() + " ya ha sido leído y no puede ser editado");
            }

            if(mensajeRepository.findById(mensaje.getId()).get().getAdoptante() != adoptanteRepository.findById(idAdoptante).get()) {
                log.warn("El mensaje con ID: {} no pertenece al adoptante con ID: {}", mensaje.getId(), idAdoptante);
                throw new IllegalArgumentException("El mensaje con ID: " + mensaje.getId() + " no pertenece al adoptante con ID: " + idAdoptante);
            }

            log.info("Editando mensaje con ID: {}", mensaje.getId());
            mensajeRepository.save(mensaje);
        } catch (Exception e) {
            log.error("Error al editar el mensaje con ID: {}", mensaje.getId(), e);
            throw new RuntimeException("Error al editar el mensaje con ID: " + mensaje.getId(), e);
        }
    }

}
