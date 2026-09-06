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
public class AdoptanteEntityTest {

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<AdoptanteEntity> data = new ArrayList<>();

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
			AdoptanteEntity entity = factory.manufacturePojo(AdoptanteEntity.class);
			entityManager.persist(entity);
			data.add(entity);
		}
	}

	@Test
	void testCreateAdoptante() {
		AdoptanteEntity newEntity = factory.manufacturePojo(AdoptanteEntity.class);
		AdoptanteEntity result = entityManager.persistFlushFind(newEntity);

		assertNotNull(result);
		assertEquals(newEntity.getNombre(), result.getNombre());
		assertEquals(newEntity.getTelefono(), result.getTelefono());
		assertEquals(newEntity.getEmail(), result.getEmail());
		assertEquals(newEntity.getDireccion(), result.getDireccion());
		assertEquals(newEntity.getCiudad(), result.getCiudad());
	}

	@Test
	void testGetAdoptante() {
		AdoptanteEntity entity = data.get(0);
		AdoptanteEntity result = entityManager.find(AdoptanteEntity.class, entity.getId());

		assertNotNull(result);
		assertEquals(entity.getNombre(), result.getNombre());
		assertEquals(entity.getEmail(), result.getEmail());
	}

	@Test
	void testUpdateAdoptante() {
		AdoptanteEntity entity = data.get(0);
		AdoptanteEntity newData = factory.manufacturePojo(AdoptanteEntity.class);

		entity.setNombre(newData.getNombre());
		entity.setTelefono(newData.getTelefono());
		entity.setEmail(newData.getEmail());
		entity.setDireccion(newData.getDireccion());
		entity.setCiudad(newData.getCiudad());
		entityManager.merge(entity);

		AdoptanteEntity resp = entityManager.find(AdoptanteEntity.class, entity.getId());
		assertEquals(newData.getNombre(), resp.getNombre());
		assertEquals(newData.getEmail(), resp.getEmail());
		assertEquals(newData.getCiudad(), resp.getCiudad());
	}

	@Test
	void testDeleteAdoptante() {
		AdoptanteEntity entity = data.get(0);
		entityManager.remove(entity);

		AdoptanteEntity deleted = entityManager.find(AdoptanteEntity.class, entity.getId());
		assertNull(deleted);
	}

}
