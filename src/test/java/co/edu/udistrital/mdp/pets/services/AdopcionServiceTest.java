package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.AdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopcionRepository;

@ExtendWith(MockitoExtension.class)
class AdopcionServiceTest {

    private static final Long ID = 7L;

    @Mock
    private AdopcionRepository adopcionRepository;

    @InjectMocks
    private AdopcionService adopcionService;

    @Test
    void testCreateAdopcion() {
        AdopcionEntity adopcion = new AdopcionEntity();
        when(adopcionRepository.save(adopcion)).thenReturn(adopcion);

        assertSame(adopcion, adopcionService.createAdopcion(adopcion));

        verify(adopcionRepository).save(adopcion);
    }

    @Test
    void testCreateAdopcionCuandoRepositorioFalla() {
        AdopcionEntity adopcion = new AdopcionEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(adopcionRepository.save(adopcion)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> adopcionService.createAdopcion(adopcion));

        assertEquals("Error al crear la adopción.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }

    @Test
    void testGetAdopciones() {
        List<AdopcionEntity> adopciones = List.of(new AdopcionEntity());
        when(adopcionRepository.findAll()).thenReturn(adopciones);

        assertSame(adopciones, adopcionService.getAdopciones());

        verify(adopcionRepository).findAll();
    }

    @Test
    void testGetAdopcionExistente() {
        AdopcionEntity adopcion = new AdopcionEntity();
        when(adopcionRepository.findById(ID)).thenReturn(Optional.of(adopcion));

        assertSame(adopcion, adopcionService.getAdopcion(ID));

        verify(adopcionRepository).findById(ID);
    }

    @Test
    void testGetAdopcionInexistente() {
        when(adopcionRepository.findById(ID)).thenReturn(Optional.empty());

        assertNull(adopcionService.getAdopcion(ID));

        verify(adopcionRepository).findById(ID);
    }

    @Test
    void testGetAdopcionesByEstado() {
        List<AdopcionEntity> adopciones = List.of(new AdopcionEntity());
        when(adopcionRepository.findByEstado("FINALIZADA")).thenReturn(adopciones);

        assertSame(adopciones, adopcionService.getAdopcionesByEstado("FINALIZADA"));

        verify(adopcionRepository).findByEstado("FINALIZADA");
    }

    @Test
    void testGetAdopcionBySolicitudExistente() {
        AdopcionEntity adopcion = new AdopcionEntity();
        when(adopcionRepository.findBySolicitudId(ID)).thenReturn(Optional.of(adopcion));

        assertSame(adopcion, adopcionService.getAdopcionBySolicitud(ID));

        verify(adopcionRepository).findBySolicitudId(ID);
    }

    @Test
    void testGetAdopcionBySolicitudInexistente() {
        when(adopcionRepository.findBySolicitudId(ID)).thenReturn(Optional.empty());

        assertNull(adopcionService.getAdopcionBySolicitud(ID));

        verify(adopcionRepository).findBySolicitudId(ID);
    }

    @Test
    void testUpdateAdopcionExistente() {
        AdopcionEntity existente = new AdopcionEntity();
        AdopcionEntity actualizada = new AdopcionEntity();
        when(adopcionRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(adopcionRepository.save(actualizada)).thenReturn(actualizada);

        AdopcionEntity result = adopcionService.updateAdopcion(ID, actualizada);

        assertSame(actualizada, result);
        assertEquals(ID, actualizada.getId());
        verify(adopcionRepository).save(actualizada);
    }

    @Test
    void testUpdateAdopcionInexistente() {
        AdopcionEntity adopcion = new AdopcionEntity();
        when(adopcionRepository.findById(ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> adopcionService.updateAdopcion(ID, adopcion));

        assertEquals("Error al actualizar la adopción.", exception.getMessage());
        assertEquals("La adopción no existe.", exception.getCause().getMessage());
        verify(adopcionRepository).findById(ID);
        verifyNoMoreInteractions(adopcionRepository);
    }

    @Test
    void testUpdateAdopcionCuandoRepositorioFalla() {
        AdopcionEntity existente = new AdopcionEntity();
        AdopcionEntity actualizada = new AdopcionEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(adopcionRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(adopcionRepository.save(actualizada)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> adopcionService.updateAdopcion(ID, actualizada));

        assertEquals("Error al actualizar la adopción.", exception.getMessage());
        assertSame(causa, exception.getCause());
        assertEquals(ID, actualizada.getId());
    }

    @Test
    void testDeleteAdopcion() {
        adopcionService.deleteAdopcion(ID);

        verify(adopcionRepository).deleteById(ID);
    }

    @Test
    void testDeleteAdopcionCuandoRepositorioFalla() {
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        doThrow(causa).when(adopcionRepository).deleteById(ID);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> adopcionService.deleteAdopcion(ID));

        assertEquals("Error al eliminar la adopción.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }
}
