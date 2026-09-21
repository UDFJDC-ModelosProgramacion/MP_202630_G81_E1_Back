package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@Transactional
public class SolicitudAdopcionServiceTest {

    @Autowired
    private SolicitudAdopcionService solicitudService;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<SolicitudAdopcionEntity> data = new ArrayList<>();

    private AdoptanteEntity adoptante;
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {

        solicitudRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        for (int i = 0; i < 3; i++) {
            SolicitudAdopcionEntity entity = factory.manufacturePojo(SolicitudAdopcionEntity.class);
            entity.setAdoptante(adoptante);
            entity.setMascota(mascota);
            data.add(solicitudRepository.save(entity));
        }
    }

    @Test
    void testCreateSolicitud() {
        SolicitudAdopcionEntity entity = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        entity.setAdoptante(adoptante);
        entity.setMascota(mascota);

        SolicitudAdopcionEntity result = solicitudService.createSolicitud(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testGetSolicitudes() {
        List<SolicitudAdopcionEntity> list = solicitudService.getSolicitudes();
        assertEquals(3, list.size());
    }

    @Test
    void testGetSolicitud() {
        SolicitudAdopcionEntity entity = data.get(0);
        SolicitudAdopcionEntity result = solicitudService.getSolicitud(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testUpdateSolicitud() {
        SolicitudAdopcionEntity entity = data.get(0);
        entity.setEstado("APROBADA");

        SolicitudAdopcionEntity result = solicitudService.updateSolicitud(entity.getId(), entity);

        assertEquals("APROBADA", result.getEstado());
    }

    @Test
    void testDeleteSolicitud() {
        SolicitudAdopcionEntity entity = data.get(0);

        solicitudService.deleteSolicitud(entity.getId());

        assertNull(solicitudService.getSolicitud(entity.getId()));
    }
}