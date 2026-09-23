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
public class NotificacionServiceTest {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<NotificacionEntity> data = new ArrayList<>();
    private AdoptanteEntity adoptante;
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        notificacionRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        for (int i = 0; i < 3; i++) {
            NotificacionEntity entity = factory.manufacturePojo(NotificacionEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            data.add(notificacionRepository.save(entity));
        }
    }

    @Test
    void testCreateNotificacion() {
        NotificacionEntity entity = factory.manufacturePojo(NotificacionEntity.class);
        entity.setMascota(mascota);
        entity.setAdoptante(adoptante);
        NotificacionEntity result = notificacionService.createNotificacion(entity);
        assertNotNull(result);
        assertEquals(entity.getMensaje(), result.getMensaje());
    }

    @Test
    void testGetNotificaciones() {
        assertEquals(3, notificacionService.getNotificaciones().size());
    }

    @Test
    void testGetNotificacion() {
        NotificacionEntity entity = data.get(0);
        assertEquals(entity.getId(), notificacionService.getNotificacion(entity.getId()).getId());
    }

    @Test
    void testUpdateNotificacion() {
        NotificacionEntity entity = data.get(0);
        entity.setCanal("SMS");
        NotificacionEntity result = notificacionService.updateNotificacion(entity.getId(), entity);
        assertEquals("SMS", result.getCanal());
    }

    @Test
    void testDeleteNotificacion() {
        NotificacionEntity entity = data.get(0);
        notificacionService.deleteNotificacion(entity.getId());
        assertNull(notificacionService.getNotificacion(entity.getId()));
    }
}