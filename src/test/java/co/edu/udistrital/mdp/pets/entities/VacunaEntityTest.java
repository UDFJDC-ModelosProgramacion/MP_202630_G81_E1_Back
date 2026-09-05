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
public class VacunaEntityTest {

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<VacunaEntity> data = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from VacunaEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			VacunaEntity entity = factory.manufacturePojo(VacunaEntity.class);
			entityManager.persist(entity);
			data.add(entity);
		}
	}

	@Test
	void testCreateVacuna() {
		VacunaEntity newEntity = factory.manufacturePojo(VacunaEntity.class);
		VacunaEntity result = entityManager.persistFlushFind(newEntity);

		assertNotNull(result);
		assertEquals(newEntity.getNombre(), result.getNombre());
		assertEquals(newEntity.getDescripcion(), result.getDescripcion());
	}

	@Test
	void testGetVacuna() {
		VacunaEntity entity = data.get(0);
		VacunaEntity result = entityManager.find(VacunaEntity.class, entity.getId());

		assertNotNull(result);
		assertEquals(entity.getNombre(), result.getNombre());
		assertEquals(entity.getDescripcion(), result.getDescripcion());
	}

	@Test
	void testUpdateVacuna() {
		VacunaEntity entity = data.get(0);
		VacunaEntity newData = factory.manufacturePojo(VacunaEntity.class);

		entity.setNombre(newData.getNombre());
		entity.setDescripcion(newData.getDescripcion());
		entityManager.merge(entity);

		VacunaEntity resp = entityManager.find(VacunaEntity.class, entity.getId());
		assertEquals(newData.getNombre(), resp.getNombre());
		assertEquals(newData.getDescripcion(), resp.getDescripcion());
	}

	@Test
	void testDeleteVacuna() {
		VacunaEntity entity = data.get(0);
		entityManager.remove(entity);

		VacunaEntity deleted = entityManager.find(VacunaEntity.class, entity.getId());
		assertNull(deleted);
	}

}
