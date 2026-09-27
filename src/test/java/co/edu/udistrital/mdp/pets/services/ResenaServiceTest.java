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
public class ResenaServiceTest {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private AdoptanteRepository adoptanteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<ResenaEntity> data = new ArrayList<>();
    private AdoptanteEntity adoptante;
    private MascotaEntity mascota;

    @BeforeEach
    void setUp() {
        resenaRepository.deleteAll();
        adoptanteRepository.deleteAll();
        mascotaRepository.deleteAll();

        adoptante = adoptanteRepository.save(factory.manufacturePojo(AdoptanteEntity.class));
        mascota = mascotaRepository.save(factory.manufacturePojo(MascotaEntity.class));

        for (int i = 0; i < 3; i++) {
            ResenaEntity entity = factory.manufacturePojo(ResenaEntity.class);
            entity.setMascota(mascota);
            entity.setAdoptante(adoptante);
            data.add(resenaRepository.save(entity));
        }
    }

    @Test
    void testCreateResena() {
        ResenaEntity entity = factory.manufacturePojo(ResenaEntity.class);
        entity.setMascota(mascota);
        entity.setAdoptante(adoptante);
        ResenaEntity result = resenaService.createResena(entity);
        assertNotNull(result);
        assertEquals(entity.getComentario(), result.getComentario());
    }

    @Test
    void testGetResenas() {
        assertEquals(3, resenaService.getResenas().size());
    }

    @Test
    void testGetResena() {
        ResenaEntity entity = data.get(0);
        assertEquals(entity.getId(), resenaService.getResena(entity.getId()).getId());
    }

    @Test
    void testUpdateResena() {
        ResenaEntity entity = data.get(0);
        entity.setComentario("Excelente experiencia");
        ResenaEntity result = resenaService.updateResena(entity.getId(), entity);
        assertEquals("Excelente experiencia", result.getComentario());
    }

    @Test
    void testDeleteResena() {
        ResenaEntity entity = data.get(0);
        resenaService.deleteResena(entity.getId());
        assertNull(resenaService.getResena(entity.getId()));
    }
}