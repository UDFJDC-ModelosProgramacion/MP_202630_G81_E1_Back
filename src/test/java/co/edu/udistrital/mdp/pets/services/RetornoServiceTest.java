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
public class RetornoServiceTest {

    @Autowired
    private RetornoService retornoService;

    @Autowired
    private RetornoRepository retornoRepository;

    @Autowired
    private AdopcionRepository adopcionRepository;

    @Autowired
    private SolicitudAdopcionRepository solicitudRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<RetornoEntity> data = new ArrayList<>();

    private AdopcionEntity adopcion;

    @BeforeEach
    void setUp() {

        retornoRepository.deleteAll();
        adopcionRepository.deleteAll();
        solicitudRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        AdoptanteEntity adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        MascotaEntity mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        SolicitudAdopcionEntity solicitud = factory.manufacturePojo(SolicitudAdopcionEntity.class);
        solicitud.setAdoptante(adoptante);
        solicitud.setMascota(mascota);
        solicitud = solicitudRepository.save(solicitud);

        adopcion = factory.manufacturePojo(AdopcionEntity.class);
        adopcion.setSolicitud(solicitud);
        adopcion = adopcionRepository.save(adopcion);

        for (int i = 0; i < 3; i++) {
            RetornoEntity entity = factory.manufacturePojo(RetornoEntity.class);
            entity.setAdopcion(adopcion);
            data.add(retornoRepository.save(entity));
        }
    }

    @Test
    void testCreateRetorno() {
        RetornoEntity entity = factory.manufacturePojo(RetornoEntity.class);
        entity.setAdopcion(adopcion);

        RetornoEntity result = retornoService.createRetorno(entity);

        assertNotNull(result);
        assertEquals(entity.getMotivo(), result.getMotivo());
    }

    @Test
    void testGetRetornos() {
        assertEquals(3, retornoService.getRetornos().size());
    }

    @Test
    void testGetRetorno() {
        RetornoEntity entity = data.get(0);
        assertEquals(entity.getId(), retornoService.getRetorno(entity.getId()).getId());
    }

    @Test
    void testUpdateRetorno() {
        RetornoEntity entity = data.get(0);
        entity.setMotivo("Cambio de domicilio");

        RetornoEntity result = retornoService.updateRetorno(entity.getId(), entity);

        assertEquals("Cambio de domicilio", result.getMotivo());
    }

    @Test
    void testDeleteRetorno() {
        RetornoEntity entity = data.get(0);
        retornoService.deleteRetorno(entity.getId());

        assertNull(retornoService.getRetorno(entity.getId()));
    }
}