package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.FotografiaEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.repositories.FotografiaRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class FotografiaServiceTest {

    @Mock
    private FotografiaRepository fotografiaRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    private FotografiaService service;

    @BeforeEach
    void setUp() {
        service = new FotografiaService(fotografiaRepository, mascotaRepository);
    }

    @Test
    void eliminarFotografiaDeletesWhenMascotaExists() {
        when(mascotaRepository.existsById(2L)).thenReturn(true);

        service.eliminarFotografia(5L, 2L);

        verify(fotografiaRepository).deleteById(5L);
    }

    @Test
    void eliminarFotografiaThrowsWhenMascotaDoesNotExist() {
        when(mascotaRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.eliminarFotografia(5L, 2L));
        verifyNoInteractions(fotografiaRepository);
    }

    @Test
    void getFotografiasbyIdReturnsPhotography() {
        FotografiaEntity fotografia = new FotografiaEntity() {};
        when(fotografiaRepository.existsById(5L)).thenReturn(true);
        when(fotografiaRepository.findById(5L)).thenReturn(Optional.of(fotografia));

        assertEquals(Optional.of(fotografia), service.getFotografiasbyId(5L));
    }

    @Test
    void getFotografiasbyIdThrowsWhenPhotographyDoesNotExist() {
        when(fotografiaRepository.existsById(5L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.getFotografiasbyId(5L));
        verifyNoInteractions(mascotaRepository);
    }

    @Test
    void setFotografiaActiveSavesExistingPhotography() {
        FotografiaEntity fotografia = new FotografiaEntity() {};
        when(fotografiaRepository.findById(5L)).thenReturn(Optional.of(fotografia));

        service.setFotografiaActive(5L, new MascotaEntity());

        verify(fotografiaRepository).save(fotografia);
    }

    @Test
    void setFotografiaActiveDoesNothingWhenPhotographyDoesNotExist() {
        when(fotografiaRepository.findById(5L)).thenReturn(Optional.empty());

        service.setFotografiaActive(5L, new MascotaEntity());

        verify(fotografiaRepository).findById(5L);
    }
}