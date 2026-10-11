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

import co.edu.udistrital.mdp.pets.entities.PruebaConvivenciaEntity;
import co.edu.udistrital.mdp.pets.repositories.PruebaConvivenciaRepository;

@ExtendWith(MockitoExtension.class)
class PruebaConvivenciaServiceTest {

    private static final Long ID = 7L;

    @Mock
    private PruebaConvivenciaRepository pruebaRepository;

    @InjectMocks
    private PruebaConvivenciaService pruebaService;

    @Test
    void testCreatePrueba() {
        PruebaConvivenciaEntity prueba = new PruebaConvivenciaEntity();
        when(pruebaRepository.save(prueba)).thenReturn(prueba);

        assertSame(prueba, pruebaService.createPrueba(prueba));

        verify(pruebaRepository).save(prueba);
    }

    @Test
    void testCreatePruebaCuandoRepositorioFalla() {
        PruebaConvivenciaEntity prueba = new PruebaConvivenciaEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(pruebaRepository.save(prueba)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> pruebaService.createPrueba(prueba));

        assertEquals("Error al crear la prueba de convivencia.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }

    @Test
    void testGetPruebas() {
        List<PruebaConvivenciaEntity> pruebas = List.of(new PruebaConvivenciaEntity());
        when(pruebaRepository.findAll()).thenReturn(pruebas);

        assertSame(pruebas, pruebaService.getPruebas());

        verify(pruebaRepository).findAll();
    }

    @Test
    void testGetPruebaExistente() {
        PruebaConvivenciaEntity prueba = new PruebaConvivenciaEntity();
        when(pruebaRepository.findById(ID)).thenReturn(Optional.of(prueba));

        assertSame(prueba, pruebaService.getPrueba(ID));

        verify(pruebaRepository).findById(ID);
    }

    @Test
    void testGetPruebaInexistente() {
        when(pruebaRepository.findById(ID)).thenReturn(Optional.empty());

        assertNull(pruebaService.getPrueba(ID));

        verify(pruebaRepository).findById(ID);
    }

    @Test
    void testGetPruebasByEstado() {
        List<PruebaConvivenciaEntity> pruebas = List.of(new PruebaConvivenciaEntity());
        when(pruebaRepository.findByEstado("FINALIZADA")).thenReturn(pruebas);

        assertSame(pruebas, pruebaService.getPruebasByEstado("FINALIZADA"));

        verify(pruebaRepository).findByEstado("FINALIZADA");
    }

    @Test
    void testGetPruebaByAdopcionExistente() {
        PruebaConvivenciaEntity prueba = new PruebaConvivenciaEntity();
        when(pruebaRepository.findByAdopcionId(ID)).thenReturn(Optional.of(prueba));

        assertSame(prueba, pruebaService.getPruebaByAdopcion(ID));

        verify(pruebaRepository).findByAdopcionId(ID);
    }

    @Test
    void testGetPruebaByAdopcionInexistente() {
        when(pruebaRepository.findByAdopcionId(ID)).thenReturn(Optional.empty());

        assertNull(pruebaService.getPruebaByAdopcion(ID));

        verify(pruebaRepository).findByAdopcionId(ID);
    }

    @Test
    void testUpdatePruebaExistente() {
        PruebaConvivenciaEntity existente = new PruebaConvivenciaEntity();
        PruebaConvivenciaEntity actualizada = new PruebaConvivenciaEntity();
        when(pruebaRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(pruebaRepository.save(actualizada)).thenReturn(actualizada);

        PruebaConvivenciaEntity result = pruebaService.updatePrueba(ID, actualizada);

        assertSame(actualizada, result);
        assertEquals(ID, actualizada.getId());
        verify(pruebaRepository).save(actualizada);
    }

    @Test
    void testUpdatePruebaInexistente() {
        PruebaConvivenciaEntity prueba = new PruebaConvivenciaEntity();
        when(pruebaRepository.findById(ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> pruebaService.updatePrueba(ID, prueba));

        assertEquals("Error al actualizar la prueba de convivencia.", exception.getMessage());
        assertEquals("La prueba de convivencia no existe.", exception.getCause().getMessage());
        verify(pruebaRepository).findById(ID);
        verifyNoMoreInteractions(pruebaRepository);
    }

    @Test
    void testUpdatePruebaCuandoRepositorioFalla() {
        PruebaConvivenciaEntity existente = new PruebaConvivenciaEntity();
        PruebaConvivenciaEntity actualizada = new PruebaConvivenciaEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(pruebaRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(pruebaRepository.save(actualizada)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> pruebaService.updatePrueba(ID, actualizada));

        assertEquals("Error al actualizar la prueba de convivencia.", exception.getMessage());
        assertSame(causa, exception.getCause());
        assertEquals(ID, actualizada.getId());
    }

    @Test
    void testDeletePrueba() {
        pruebaService.deletePrueba(ID);

        verify(pruebaRepository).deleteById(ID);
    }

    @Test
    void testDeletePruebaCuandoRepositorioFalla() {
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        doThrow(causa).when(pruebaRepository).deleteById(ID);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> pruebaService.deletePrueba(ID));

        assertEquals("Error al eliminar la prueba de convivencia.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }
}
