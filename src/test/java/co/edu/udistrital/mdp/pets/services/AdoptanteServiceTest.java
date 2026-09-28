package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de la lógica de la clase AdoptanteService.
 */
@DataJpaTest
@Transactional
@Import(AdoptanteService.class)
class AdoptanteServiceTest {

	@Autowired
	private AdoptanteService adoptanteService;

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<AdoptanteEntity> adoptanteList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from AdoptanteEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			AdoptanteEntity adoptante = factory.manufacturePojo(AdoptanteEntity.class);
			adoptante.setEmail("adoptante" + i + "@correo.com");
			entityManager.persist(adoptante);
			adoptanteList.add(adoptante);
		}
	}

	// ---------- createAdoptante ----------

	@Test
	void testCreateAdoptante() throws IllegalOperationException {
		AdoptanteEntity nuevo = factory.manufacturePojo(AdoptanteEntity.class);
		nuevo.setNombre("Camila Rios");
		nuevo.setEmail("nuevo" + System.nanoTime() + "@correo.com");

		AdoptanteEntity creado = adoptanteService.createAdoptante(nuevo);

		assertEquals(nuevo.getEmail(), creado.getEmail());
		AdoptanteEntity encontrado = entityManager.find(AdoptanteEntity.class, creado.getId());
		assertEquals(nuevo.getNombre(), encontrado.getNombre());
	}

	@Test
	void testCreateAdoptanteConNombreVacio() {
		AdoptanteEntity nuevo = factory.manufacturePojo(AdoptanteEntity.class);
		nuevo.setNombre("");
		nuevo.setEmail("otro" + System.nanoTime() + "@correo.com");

		assertThrows(IllegalOperationException.class, () -> adoptanteService.createAdoptante(nuevo));
	}

	@Test
	void testCreateAdoptanteConEmailInvalido() {
		AdoptanteEntity nuevo = factory.manufacturePojo(AdoptanteEntity.class);
		nuevo.setEmail("correo-sin-formato-valido");

		assertThrows(IllegalOperationException.class, () -> adoptanteService.createAdoptante(nuevo));
	}

	@Test
	void testCreateAdoptanteConEmailDuplicado() {
		AdoptanteEntity nuevo = factory.manufacturePojo(AdoptanteEntity.class);
		nuevo.setEmail(adoptanteList.get(0).getEmail());

		assertThrows(IllegalOperationException.class, () -> adoptanteService.createAdoptante(nuevo));
	}

	// ---------- getAdoptantes ----------

	@Test
	void testGetAdoptantes() {
		List<AdoptanteEntity> lista = adoptanteService.getAdoptantes();
		assertEquals(adoptanteList.size(), lista.size());
	}

	// ---------- getAdoptante ----------

	@Test
	void testGetAdoptante() throws EntityNotFoundException {
		AdoptanteEntity esperado = adoptanteList.get(0);
		AdoptanteEntity obtenido = adoptanteService.getAdoptante(esperado.getId());
		assertEquals(esperado.getEmail(), obtenido.getEmail());
	}

	@Test
	void testGetAdoptanteNoExistente() {
		assertThrows(EntityNotFoundException.class, () -> adoptanteService.getAdoptante(0L));
	}

	// ---------- updateAdoptante ----------

	@Test
	void testUpdateAdoptante() throws EntityNotFoundException, IllegalOperationException {
		AdoptanteEntity adoptante = adoptanteList.get(0);
		AdoptanteEntity nuevosDatos = factory.manufacturePojo(AdoptanteEntity.class);
		nuevosDatos.setNombre("Nombre Actualizado");
		nuevosDatos.setEmail("actualizado" + System.nanoTime() + "@correo.com");

		AdoptanteEntity actualizado = adoptanteService.updateAdoptante(adoptante.getId(), nuevosDatos);

		assertEquals(adoptante.getId(), actualizado.getId());
		assertEquals(nuevosDatos.getNombre(), actualizado.getNombre());
	}

	@Test
	void testUpdateAdoptanteNoExistente() {
		AdoptanteEntity nuevosDatos = factory.manufacturePojo(AdoptanteEntity.class);
		assertThrows(EntityNotFoundException.class, () -> adoptanteService.updateAdoptante(0L, nuevosDatos));
	}

	@Test
	void testUpdateAdoptanteConEmailInvalido() {
		AdoptanteEntity adoptante = adoptanteList.get(0);
		AdoptanteEntity nuevosDatos = factory.manufacturePojo(AdoptanteEntity.class);
		nuevosDatos.setEmail("email-invalido");

		assertThrows(IllegalOperationException.class,
				() -> adoptanteService.updateAdoptante(adoptante.getId(), nuevosDatos));
	}

	@Test
	void testUpdateAdoptanteConEmailDuplicado() {
		AdoptanteEntity adoptante = adoptanteList.get(0);
		AdoptanteEntity nuevosDatos = factory.manufacturePojo(AdoptanteEntity.class);
		nuevosDatos.setEmail(adoptanteList.get(1).getEmail());

		assertThrows(IllegalOperationException.class,
				() -> adoptanteService.updateAdoptante(adoptante.getId(), nuevosDatos));
	}

	// ---------- deleteAdoptante ----------

	@Test
	void testDeleteAdoptante() throws EntityNotFoundException, IllegalOperationException {
		AdoptanteEntity adoptante = adoptanteList.get(0);
		adoptanteService.deleteAdoptante(adoptante.getId());
		assertNull(entityManager.find(AdoptanteEntity.class, adoptante.getId()));
	}

	@Test
	void testDeleteAdoptanteNoExistente() {
		assertThrows(EntityNotFoundException.class, () -> adoptanteService.deleteAdoptante(0L));
	}

	// Pendiente: prueba de "no se puede eliminar con solicitudes asociadas una vez SolicitudAdopcionEntity deje de ser un placeholder temporal.
}