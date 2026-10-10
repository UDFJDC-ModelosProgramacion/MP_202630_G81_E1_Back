package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.eliminarFotografia(5L, 2L));

        assertEquals("Error al eliminar la fotografía con ID: 5", exception.getMessage());
        assertEquals("La mascota con ID: 2 no existe", exception.getCause().getMessage());
        verifyNoInteractions(fotografiaRepository);
    }

    @Test
    void eliminarFotografiaWrapsMascotaRepositoryFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.existsById(2L)).thenThrow(failure);

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.eliminarFotografia(5L, 2L));

        assertEquals("Error al eliminar la fotografía con ID: 5", exception.getMessage());
        assertSame(failure, exception.getCause());
        verify(fotografiaRepository, never()).deleteById(5L);
    }

    @Test
    void eliminarFotografiaWrapsDeleteFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.existsById(2L)).thenReturn(true);
        doThrow(failure).when(fotografiaRepository).deleteById(5L);

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.eliminarFotografia(5L, 2L));

        assertEquals("Error al eliminar la fotografía con ID: 5", exception.getMessage());
        assertSame(failure, exception.getCause());
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

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.getFotografiasbyId(5L));

        assertEquals("Error al obtener la fotografía con ID: 5", exception.getMessage());
        assertEquals("La fotografía con ID: 5 no existe", exception.getCause().getMessage());
        verifyNoInteractions(mascotaRepository);
    }

    @Test
    void getFotografiasbyIdWrapsExistsCheckFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(fotografiaRepository.existsById(5L)).thenThrow(failure);

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.getFotografiasbyId(5L));

        assertEquals("Error al obtener la fotografía con ID: 5", exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void getFotografiasbyIdWrapsFindFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(fotografiaRepository.existsById(5L)).thenReturn(true);
        when(fotografiaRepository.findById(5L)).thenThrow(failure);

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.getFotografiasbyId(5L));

        assertEquals("Error al obtener la fotografía con ID: 5", exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void getFotografiasbyIdReturnsEmptyWhenRecordDisappearsAfterExistsCheck() {
        when(fotografiaRepository.existsById(5L)).thenReturn(true);
        when(fotografiaRepository.findById(5L)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), service.getFotografiasbyId(5L));
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

    @Test
    void setFotografiaActiveWrapsFindFailure() {
        RuntimeException failure = new IllegalStateException("database failure");
        when(fotografiaRepository.findById(5L)).thenThrow(failure);

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.setFotografiaActive(5L, new MascotaEntity()));

        assertEquals("Error al activar la fotografía con ID: 5", exception.getMessage());
        assertSame(failure, exception.getCause());
        verify(fotografiaRepository, never()).save(any());
    }

    @Test
    void setFotografiaActiveWrapsSaveFailure() {
        FotografiaEntity fotografia = new FotografiaEntity() {};
        RuntimeException failure = new IllegalStateException("database failure");
        when(fotografiaRepository.findById(5L)).thenReturn(Optional.of(fotografia));
        when(fotografiaRepository.save(fotografia)).thenThrow(failure);

        FotografiaService.FotografiaException exception = assertThrows(
                FotografiaService.FotografiaException.class,
                () -> service.setFotografiaActive(5L, new MascotaEntity()));

        assertEquals("Error al activar la fotografía con ID: 5", exception.getMessage());
        assertSame(failure, exception.getCause());
    }
}