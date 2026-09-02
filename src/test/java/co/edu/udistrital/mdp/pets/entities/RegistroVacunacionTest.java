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
public class RegistroVacunacionTest {

	@Autowired
	private TestEntityManager entityManager;

	private final PodamFactory factory = new PodamFactoryImpl();

	private final List<RegistroVacunacion> data = new ArrayList<>();

	private Vacuna vacuna;

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from RegistroVacunacion").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from Vacuna").executeUpdate();
	}

	private void insertData() {
		vacuna = factory.manufacturePojo(Vacuna.class);
		entityManager.persist(vacuna);

		for (int i = 0; i < 3; i++) {
			RegistroVacunacion entity = factory.manufacturePojo(RegistroVacunacion.class);
			entity.setVacuna(vacuna);
			entityManager.persist(entity);
			data.add(entity);
		}
	}

	@Test
	void testCreateRegistroVacunacion() {
	    RegistroVacunacion newEntity = factory.manufacturePojo(RegistroVacunacion.class);
	    newEntity.setVacuna(vacuna);

	    RegistroVacunacion result = entityManager.persistFlushFind(newEntity);

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
		RegistroVacunacion entity = data.get(0);
		RegistroVacunacion result = entityManager.find(RegistroVacunacion.class, entity.getId());

		assertNotNull(result);
		assertEquals(entity.getNumeroLote(), result.getNumeroLote());
		assertEquals(vacuna.getId(), result.getVacuna().getId());
	}

	@Test
	void testUpdateRegistroVacunacion() {
		RegistroVacunacion entity = data.get(0);
		RegistroVacunacion newData = factory.manufacturePojo(RegistroVacunacion.class);

		entity.setNumeroLote(newData.getNumeroLote());
		entity.setObservacion(newData.getObservacion());
		entityManager.merge(entity);

		RegistroVacunacion resp = entityManager.find(RegistroVacunacion.class, entity.getId());
		assertEquals(newData.getNumeroLote(), resp.getNumeroLote());
		assertEquals(newData.getObservacion(), resp.getObservacion());
	}

	@Test
	void testDeleteRegistroVacunacion() {
		RegistroVacunacion entity = data.get(0);
		entityManager.remove(entity);

		RegistroVacunacion deleted = entityManager.find(RegistroVacunacion.class, entity.getId());
		assertNull(deleted);
	}

}
