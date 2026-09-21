package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.RegistroVacunacionEntity;
import co.edu.udistrital.mdp.pets.entities.VacunaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de la lógica de la clase RegistroVacunacionService.
 */
@DataJpaTest
@Transactional
@Import(RegistroVacunacionService.class)
class RegistroVacunacionServiceTest {

	@Autowired
	private RegistroVacunacionService registroVacunacionService;

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<RegistroVacunacionEntity> registroList = new ArrayList<>();

	private VacunaEntity vacuna;
	private MascotaEntity mascota;

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from RegistroVacunacionEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VacunaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from MascotaEntity").executeUpdate();
	}

	private void insertData() {
		vacuna = factory.manufacturePojo(VacunaEntity.class);
		entityManager.persist(vacuna);

		mascota = factory.manufacturePojo(MascotaEntity.class);
		entityManager.persist(mascota);

		for (int i = 0; i < 3; i++) {
			RegistroVacunacionEntity registro = factory.manufacturePojo(RegistroVacunacionEntity.class);
			registro.setVacuna(vacuna);
			registro.setMascota(mascota);
			registro.setFechaAplicacion(new Date(System.currentTimeMillis() - 100000L));
			registro.setProximaFecha(null);
			registro.setNumeroLote("LOTE-" + i);
			entityManager.persist(registro);
			registroList.add(registro);
		}
	}

	private RegistroVacunacionEntity nuevoRegistroValido() {
		RegistroVacunacionEntity registro = factory.manufacturePojo(RegistroVacunacionEntity.class);
		registro.setVacuna(vacuna);
		registro.setMascota(mascota);
		registro.setFechaAplicacion(new Date(System.currentTimeMillis() - 100000L));
		registro.setProximaFecha(new Date(System.currentTimeMillis() + 1000000000L));
		registro.setNumeroLote("LOTE-NUEVO");
		return registro;
	}

	// ---------- createRegistroVacunacion ----------

	@Test
	void testCreateRegistroVacunacion() throws IllegalOperationException, EntityNotFoundException {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();

		RegistroVacunacionEntity creado = registroVacunacionService.createRegistroVacunacion(nuevo);

		assertEquals(nuevo.getNumeroLote(), creado.getNumeroLote());
		RegistroVacunacionEntity encontrado = entityManager.find(RegistroVacunacionEntity.class, creado.getId());
		assertEquals(vacuna.getId(), encontrado.getVacuna().getId());
	}

	@Test
	void testCreateRegistroVacunacionSinFechaAplicacion() {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();
		nuevo.setFechaAplicacion(null);

		assertThrows(IllegalOperationException.class,
				() -> registroVacunacionService.createRegistroVacunacion(nuevo));
	}

	@Test
	void testCreateRegistroVacunacionConFechaFutura() {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();
		nuevo.setFechaAplicacion(new Date(System.currentTimeMillis() + 1000000000L));

		assertThrows(IllegalOperationException.class,
				() -> registroVacunacionService.createRegistroVacunacion(nuevo));
	}

	@Test
	void testCreateRegistroVacunacionConProximaFechaInvalida() {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();
		nuevo.setProximaFecha(new Date(nuevo.getFechaAplicacion().getTime() - 1000L));

		assertThrows(IllegalOperationException.class,
				() -> registroVacunacionService.createRegistroVacunacion(nuevo));
	}

	@Test
	void testCreateRegistroVacunacionSinNumeroLote() {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();
		nuevo.setNumeroLote("");

		assertThrows(IllegalOperationException.class,
				() -> registroVacunacionService.createRegistroVacunacion(nuevo));
	}

	@Test
	void testCreateRegistroVacunacionConVacunaInexistente() {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();
		VacunaEntity vacunaFalsa = new VacunaEntity();
		vacunaFalsa.setId(0L);
		nuevo.setVacuna(vacunaFalsa);

		assertThrows(EntityNotFoundException.class,
				() -> registroVacunacionService.createRegistroVacunacion(nuevo));
	}

	@Test
	void testCreateRegistroVacunacionConMascotaInexistente() {
		RegistroVacunacionEntity nuevo = nuevoRegistroValido();
		MascotaEntity mascotaFalsa = new MascotaEntity();
		mascotaFalsa.setId(0L);
		nuevo.setMascota(mascotaFalsa);

		assertThrows(EntityNotFoundException.class,
				() -> registroVacunacionService.createRegistroVacunacion(nuevo));
	}

	// ---------- getRegistrosVacunacion ----------

	@Test
	void testGetRegistrosVacunacion() {
		List<RegistroVacunacionEntity> lista = registroVacunacionService.getRegistrosVacunacion();
		assertEquals(registroList.size(), lista.size());
	}

	// ---------- getRegistroVacunacion ----------

	@Test
	void testGetRegistroVacunacion() throws EntityNotFoundException {
		RegistroVacunacionEntity esperado = registroList.get(0);
		RegistroVacunacionEntity obtenido = registroVacunacionService.getRegistroVacunacion(esperado.getId());
		assertEquals(esperado.getNumeroLote(), obtenido.getNumeroLote());
	}

	@Test
	void testGetRegistroVacunacionNoExistente() {
		assertThrows(EntityNotFoundException.class, () -> registroVacunacionService.getRegistroVacunacion(0L));
	}

	// ---------- updateRegistroVacunacion ----------

	@Test
	void testUpdateRegistroVacunacion() throws EntityNotFoundException, IllegalOperationException {
		RegistroVacunacionEntity registro = registroList.get(0);
		RegistroVacunacionEntity nuevosDatos = nuevoRegistroValido();
		nuevosDatos.setObservacion("Observación actualizada");

		RegistroVacunacionEntity actualizado = registroVacunacionService
				.updateRegistroVacunacion(registro.getId(), nuevosDatos);

		assertEquals(registro.getId(), actualizado.getId());
		assertEquals("Observación actualizada", actualizado.getObservacion());
	}

	@Test
	void testUpdateRegistroVacunacionNoExistente() {
		RegistroVacunacionEntity nuevosDatos = nuevoRegistroValido();
		assertThrows(EntityNotFoundException.class,
				() -> registroVacunacionService.updateRegistroVacunacion(0L, nuevosDatos));
	}

	@Test
	void testUpdateRegistroVacunacionConDatosInvalidos() {
		RegistroVacunacionEntity registro = registroList.get(0);
		RegistroVacunacionEntity nuevosDatos = nuevoRegistroValido();
		nuevosDatos.setNumeroLote("");

		assertThrows(IllegalOperationException.class,
				() -> registroVacunacionService.updateRegistroVacunacion(registro.getId(), nuevosDatos));
	}

	// ---------- deleteRegistroVacunacion ----------

	@Test
	void testDeleteRegistroVacunacion() throws EntityNotFoundException {
		RegistroVacunacionEntity registro = registroList.get(0);
		registroVacunacionService.deleteRegistroVacunacion(registro.getId());
		assertNull(entityManager.find(RegistroVacunacionEntity.class, registro.getId()));
	}

	@Test
	void testDeleteRegistroVacunacionNoExistente() {
		assertThrows(EntityNotFoundException.class,
				() -> registroVacunacionService.deleteRegistroVacunacion(0L));
	}
}