package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptanteEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopcionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@SpringBootTest
@Transactional
public class AdopcionServiceTest {

    @Autowired
    private AdopcionService adopcionService;

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<AdopcionEntity> data = new ArrayList<>();

    private SolicitudAdopcionEntity solicitud;

    @BeforeEach
    void setUp() {

        adopcionRepository.deleteAll();
        solicitudRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        // Solicitud independiente para testCreateAdopcion
        AdoptanteEntity adoptante =
                adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));

        MascotaEntity mascota =
                mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        solicitud = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);

        solicitud = solicitudRepository.save(solicitud);

        // Cada adopción tendrá una solicitud diferente
        for (int i = 0; i < 3; i++) {

            AdoptanteEntity nuevoAdoptante =
                    adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));

            MascotaEntity nuevaMascota =
                    mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

            SolicitudAdopcionEntity nuevaSolicitud =
                    factory.manufacturePojo(SolicitudAdopcionEntity.class);

            nuevaSolicitud.setAdoptante(nuevoAdoptante);
            nuevaSolicitud.setMascota(nuevaMascota);

            nuevaSolicitud = solicitudRepository.save(nuevaSolicitud);

            AdopcionEntity entity =
                    factory.manufacturePojo(AdopcionEntity.class);

            entity.setSolicitud(nuevaSolicitud);

            data.add(adopcionRepository.save(entity));
        }
    }

    @Test
    void testCreateAdopcion() {

        AdoptanteEntity adoptante =
                adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));

        MascotaEntity mascota =
                mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        SolicitudAdopcionEntity nuevaSolicitud =
                factory.manufacturePojo(SolicitudAdopcionEntity.class);

        nuevaSolicitud.setAdoptante(adoptante);
        nuevaSolicitud.setMascota(mascota);

        nuevaSolicitud = solicitudRepository.save(nuevaSolicitud);

        AdopcionEntity entity =
                factory.manufacturePojo(AdopcionEntity.class);

        entity.setSolicitud(nuevaSolicitud);

        AdopcionEntity result = adopcionService.createAdopcion(entity);

        assertNotNull(result);
        assertEquals(entity.getEstado(), result.getEstado());
    }

    @Test
    void testGetAdopciones() {
        List<AdopcionEntity> list = adopcionService.getAdopciones();
        assertEquals(3, list.size());
    }

    @Test
    void testGetAdopcion() {
        AdopcionEntity entity = data.get(0);

        AdopcionEntity result = adopcionService.getAdopcion(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testUpdateAdopcion() {

        AdopcionEntity entity = data.get(0);
        entity.setEstado("FINALIZADA");

        AdopcionEntity result =
                adopcionService.updateAdopcion(entity.getId(), entity);

        assertNotNull(result);
        assertEquals("FINALIZADA", result.getEstado());
    }

    @Test
    void testDeleteAdopcion() {

        AdopcionEntity entity = data.get(0);

        adopcionService.deleteAdopcion(entity.getId());

        AdopcionEntity deleted =
                adopcionService.getAdopcion(entity.getId());

        assertNull(deleted);
    }
}