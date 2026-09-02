package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de persistencia para la entidad Adoptante.
 */
@DataJpaTest
@Transactional
public class AdoptanteTest {

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<Adoptante> data = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from Adoptante").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			Adoptante entity = factory.manufacturePojo(Adoptante.class);
			entityManager.persist(entity);
			data.add(entity);
		}
	}

	@Test
	void testCreateAdoptante() {
		Adoptante newEntity = factory.manufacturePojo(Adoptante.class);
		Adoptante result = entityManager.persistFlushFind(newEntity);

		assertNotNull(result);
		assertEquals(newEntity.getNombre(), result.getNombre());
		assertEquals(newEntity.getTelefono(), result.getTelefono());
		assertEquals(newEntity.getEmail(), result.getEmail());
		assertEquals(newEntity.getDireccion(), result.getDireccion());
		assertEquals(newEntity.getCiudad(), result.getCiudad());
	}

	@Test
	void testGetAdoptante() {
		Adoptante entity = data.get(0);
		Adoptante result = entityManager.find(Adoptante.class, entity.getId());

		assertNotNull(result);
		assertEquals(entity.getNombre(), result.getNombre());
		assertEquals(entity.getEmail(), result.getEmail());
	}

	@Test
	void testUpdateAdoptante() {
		Adoptante entity = data.get(0);
		Adoptante newData = factory.manufacturePojo(Adoptante.class);

		entity.setNombre(newData.getNombre());
		entity.setTelefono(newData.getTelefono());
		entity.setEmail(newData.getEmail());
		entity.setDireccion(newData.getDireccion());
		entity.setCiudad(newData.getCiudad());
		entityManager.merge(entity);

		Adoptante resp = entityManager.find(Adoptante.class, entity.getId());
		assertEquals(newData.getNombre(), resp.getNombre());
		assertEquals(newData.getEmail(), resp.getEmail());
		assertEquals(newData.getCiudad(), resp.getCiudad());
	}

	@Test
	void testDeleteAdoptante() {
		Adoptante entity = data.get(0);
		entityManager.remove(entity);

		Adoptante deleted = entityManager.find(Adoptante.class, entity.getId());
		assertNull(deleted);
	}

}
