package co.edu.udistrital.mdp.pets.notifications;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias del patrón Strategy usado para el envío de
 * notificaciones. No requieren contexto de Spring ni base de datos: cada
 * implementación es una estrategia concreta que se prueba de forma
 * independiente.
 */
class CanalNotificacionTest {

	@Test
	void testNotificacionEmailEnviaCorrectamente() {
		CanalNotificacion canal = new NotificacionEmail();
		assertTrue(canal.enviar("adoptante@correo.com", "Tu solicitud fue aprobada"));
	}

	@Test
	void testNotificacionEmailRechazaDestinatarioVacio() {
		CanalNotificacion canal = new NotificacionEmail();
		assertFalse(canal.enviar("", "mensaje"));
	}

	@Test
	void testNotificacionSMSEnviaCorrectamente() {
		CanalNotificacion canal = new NotificacionSMS();
		assertTrue(canal.enviar("3011234567", "Tu mascota tiene cita de control"));
	}

	@Test
	void testNotificacionSMSRechazaDestinatarioNulo() {
		CanalNotificacion canal = new NotificacionSMS();
		assertFalse(canal.enviar(null, "mensaje"));
	}

	@Test
	void testNotificacionPushEnviaCorrectamente() {
		CanalNotificacion canal = new NotificacionPush();
		assertTrue(canal.enviar("device-token-123", "Nueva actualización disponible"));
	}

	@Test
	void testNotificacionPushRechazaDestinatarioVacio() {
		CanalNotificacion canal = new NotificacionPush();
		assertFalse(canal.enviar("   ", "mensaje"));
	}

	@Test
	void testEstrategiasSonIntercambiables() {
		// Demuestra el objetivo del patrón Strategy: el mismo contrato
		// (CanalNotificacion) permite usar distintas implementaciones
		// de forma intercambiable sin cambiar el código cliente.
		CanalNotificacion[] canales = { new NotificacionEmail(), new NotificacionSMS(), new NotificacionPush() };

		for (CanalNotificacion canal : canales) {
			assertTrue(canal.enviar("destino-valido", "mensaje de prueba"));
		}
	}

}
