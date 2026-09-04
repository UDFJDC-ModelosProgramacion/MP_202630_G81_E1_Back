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
import java.util.Calendar;
import java.util.Date;
 /**
 * Pruebas de persistencia para la entidad RegistroVacunacion, incluyendo su
 * relación @ManyToOne con Vacuna.
 */
@DataJpaTest
@Transactional
public class RegistroVacunacionEntityTest {

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<RegistroVacunacionEntity> data = new ArrayList<>();

	private VacunaEntity vacuna;

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from RegistroVacunacionEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VacunaEntity").executeUpdate();
	}

	private void insertData() {
		vacuna = factory.manufacturePojo(VacunaEntity.class);
		entityManager.persist(vacuna);

		for (int i = 0; i < 3; i++) {
			RegistroVacunacionEntity entity = factory.manufacturePojo(RegistroVacunacionEntity.class);
			entity.setVacuna(vacuna);
			entityManager.persist(entity);
			data.add(entity);
		}
	}

	@Test
	void testCreateRegistroVacunacion() {
	    RegistroVacunacionEntity newEntity = factory.manufacturePojo(RegistroVacunacionEntity.class);
	    newEntity.setVacuna(vacuna);

	    RegistroVacunacionEntity result = entityManager.persistFlushFind(newEntity);

	    assertNotNull(result);
	    // fechaAplicacion/proximaFecha están mapeadas como @Temporal(TemporalType.DATE),
	    // así que la base de datos descarta la hora. Truncamos antes de comparar.
	    assertEquals(truncarAFecha(newEntity.getFechaAplicacion()), truncarAFecha(result.getFechaAplicacion()));
	    assertEquals(truncarAFecha(newEntity.getProximaFecha()), truncarAFecha(result.getProximaFecha()));
	    assertEquals(newEntity.getNumeroLote(), result.getNumeroLote());
	    assertEquals(newEntity.getObservacion(), result.getObservacion());
	    assertEquals(vacuna.getId(), result.getVacuna().getId());
	}

	private Date truncarAFecha(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}

	@Test
	void testGetRegistroVacunacion() {
		RegistroVacunacionEntity entity = data.get(0);
		RegistroVacunacionEntity result = entityManager.find(RegistroVacunacionEntity.class, entity.getId());

		assertNotNull(result);
		assertEquals(entity.getNumeroLote(), result.getNumeroLote());
		assertEquals(vacuna.getId(), result.getVacuna().getId());
	}

	@Test
	void testUpdateRegistroVacunacion() {
		RegistroVacunacionEntity entity = data.get(0);
		RegistroVacunacionEntity newData = factory.manufacturePojo(RegistroVacunacionEntity.class);

		entity.setNumeroLote(newData.getNumeroLote());
		entity.setObservacion(newData.getObservacion());
		entityManager.merge(entity);

		RegistroVacunacionEntity resp = entityManager.find(RegistroVacunacionEntity.class, entity.getId());
		assertEquals(newData.getNumeroLote(), resp.getNumeroLote());
		assertEquals(newData.getObservacion(), resp.getObservacion());
	}

	@Test
	void testDeleteRegistroVacunacion() {
		RegistroVacunacionEntity entity = data.get(0);
		entityManager.remove(entity);

		RegistroVacunacionEntity deleted = entityManager.find(RegistroVacunacionEntity.class, entity.getId());
		assertNull(deleted);
	}

}
