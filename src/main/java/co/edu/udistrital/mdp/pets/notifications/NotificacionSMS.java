package co.edu.udistrital.mdp.pets.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Estrategia concreta para el envío de notificaciones por SMS.
 */
@Component("notificacionSMS")
public class NotificacionSMS implements CanalNotificacion {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificacionSMS.class);

    @Override
    public boolean enviar(String destinatario, String mensaje) {
        if (destinatario == null || destinatario.isBlank()) {
            return false;
        }
        // Pendiente: integrar con un proveedor real de SMS (p.ej. Twilio)
        LOGGER.info("Enviando SMS a {}: {}", destinatario, mensaje);
        return true;
    }
}