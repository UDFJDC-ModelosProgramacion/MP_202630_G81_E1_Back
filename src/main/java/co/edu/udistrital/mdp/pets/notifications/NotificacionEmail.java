package co.edu.udistrital.mdp.pets.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el envío de notificaciones por correo electrónico.
 */
@Component("notificacionEmail")
public class NotificacionEmail implements CanalNotificacion {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionEmail.class);

    @Override
    public boolean enviar(String destinatario, String mensaje) {
        if (destinatario == null || destinatario.isBlank()) {
            return false;
        }
        // Pendiente: integrar con un proveedor real de correo (p.ej. JavaMailSender)
        LOGGER.info("Enviando EMAIL a {}: {}", destinatario, mensaje);
        return true;
    }
}