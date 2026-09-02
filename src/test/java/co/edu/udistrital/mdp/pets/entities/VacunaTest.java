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
 * Pruebas de persistencia para la entidad Vacuna.
 */
@DataJpaTest
@Transactional
public class VacunaTest {

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<Vacuna> data = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from Vacuna").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			Vacuna entity = factory.manufacturePojo(Vacuna.class);
			entityManager.persist(entity);
			data.add(entity);
		}
	}

	@Test
	void testCreateVacuna() {
		Vacuna newEntity = factory.manufacturePojo(Vacuna.class);
		Vacuna result = entityManager.persistFlushFind(newEntity);

		assertNotNull(result);
		assertEquals(newEntity.getNombre(), result.getNombre());
		assertEquals(newEntity.getDescripcion(), result.getDescripcion());
	}

	@Test
	void testGetVacuna() {
		Vacuna entity = data.get(0);
		Vacuna result = entityManager.find(Vacuna.class, entity.getId());

		assertNotNull(result);
		assertEquals(entity.getNombre(), result.getNombre());
		assertEquals(entity.getDescripcion(), result.getDescripcion());
	}

	@Test
	void testUpdateVacuna() {
		Vacuna entity = data.get(0);
		Vacuna newData = factory.manufacturePojo(Vacuna.class);

		entity.setNombre(newData.getNombre());
		entity.setDescripcion(newData.getDescripcion());
		entityManager.merge(entity);

		Vacuna resp = entityManager.find(Vacuna.class, entity.getId());
		assertEquals(newData.getNombre(), resp.getNombre());
		assertEquals(newData.getDescripcion(), resp.getDescripcion());
	}

	@Test
	void testDeleteVacuna() {
		Vacuna entity = data.get(0);
		entityManager.remove(entity);

		Vacuna deleted = entityManager.find(Vacuna.class, entity.getId());
		assertNull(deleted);
	}

}
