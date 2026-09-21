package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.repositories.*;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@Transactional
public class EventoMedicoServiceTest {

    @Autowired
    private EventoMedicoService eventoMedicoService;

    @Autowired
    private EventoMedicoRepository eventoMedicoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<EventoMedicoEntity> data = new ArrayList<>();
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        eventoMedicoRepository.deleteAll();
        mascotaRepository.deleteAll();

        mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        for (int i = 0; i < 3; i++) {
            EventoMedicoEntity entity = factory.manufacturePojo(EventoMedicoEntity.class);
            entity.setMascota(mascota);
            data.add(eventoMedicoRepository.save(entity));
        }
    }

    @Test
    void testCreateEventoMedico() {
        EventoMedicoEntity entity = factory.manufacturePojo(EventoMedicoEntity.class);
        entity.setMascota(mascota);
        EventoMedicoEntity result = eventoMedicoService.createEventoMedico(entity);
        assertNotNull(result);
        assertEquals(entity.getDiagnostico(), result.getDiagnostico());
    }

    @Test
    void testGetEventosMedicos() {
        assertEquals(3, eventoMedicoService.getEventosMedicos().size());
    }

    @Test
    void testGetEventoMedico() {
        EventoMedicoEntity entity = data.get(0);
        assertEquals(entity.getId(), eventoMedicoService.getEventoMedico(entity.getId()).getId());
    }

    @Test
    void testUpdateEventoMedico() {
        EventoMedicoEntity entity = data.get(0);
        entity.setTratamiento("Antibiótico");
        EventoMedicoEntity result = eventoMedicoService.updateEventoMedico(entity.getId(), entity);
        assertEquals("Antibiótico", result.getTratamiento());
    }

    @Test
    void testDeleteEventoMedico() {
        EventoMedicoEntity entity = data.get(0);
        eventoMedicoService.deleteEventoMedico(entity.getId());
        assertNull(eventoMedicoService.getEventoMedico(entity.getId()));
    }
}