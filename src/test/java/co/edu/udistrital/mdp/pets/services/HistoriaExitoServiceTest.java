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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.HistoriaExitoEntity;
import co.edu.udistrital.mdp.pets.entities.MascotaEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptanteRepository;
import co.edu.udistrital.mdp.pets.repositories.HistoriaExitoRepository;
import co.edu.udistrital.mdp.pets.repositories.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class HistoriaExitoServiceTest {

    @Mock
    private HistoriaExitoRepository historiaExitoRepository;

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private AdoptanteRepository adoptanteRepository;

    private HistoriaExitoService service;

    @BeforeEach
    void setUp() {
        service = new HistoriaExitoService(historiaExitoRepository, mascotaRepository);
    }

    @Test
    void crearHistoriaExitoSavesHistoryForActiveMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        service.crearHistoriaExito(1L, "Adopción feliz", "Una nueva familia");

        verify(historiaExitoRepository).save(any(HistoriaExitoEntity.class));
    }

    @Test
    void crearHistoriaExitoRejectsInactiveMascota() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Inactivo");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        assertThrows(HistoriaExitoService.HistoriaExitoException.class,
                () -> service.crearHistoriaExito(1L, "Título", "Descripción"));
        verifyNoInteractions(historiaExitoRepository);
    }

    @Test
    void crearHistoriaExitoRejectsMissingMascota() {
        when(mascotaRepository.findById(1L)).thenReturn(Optional.empty());

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.crearHistoriaExito(1L, "Título", "Descripción"));

        assertEquals("La mascota con ID: 1 no existe", exception.getMessage());
        verifyNoInteractions(historiaExitoRepository);
    }

    @Test
    void crearHistoriaExitoWrapsRepositoryFailure() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.save(any(HistoriaExitoEntity.class))).thenThrow(failure);

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.crearHistoriaExito(1L, "Título", "Descripción"));

        assertEquals("Error al crear la historia de éxito para la mascota con ID: 1",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void eliminarHistoriaExitoDeletesAssociatedHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        HistoriaExitoEntity history = new HistoriaExitoEntity();
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(history);

        service.eliminarHistoriaExito(2L, 1L);

        verify(historiaExitoRepository).deleteById(2L);
    }

    @Test
    void eliminarHistoriaExitoRejectsMissingHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(null);

        assertThrows(HistoriaExitoService.HistoriaExitoException.class,
                () -> service.eliminarHistoriaExito(2L, 1L));
        verify(historiaExitoRepository).findByMascotaIdAndId(1L, 2L);
        verify(historiaExitoRepository, never()).deleteById(2L);
    }

    @Test
    void eliminarHistoriaExitoWrapsRepositoryFailure() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        HistoriaExitoEntity history = new HistoriaExitoEntity();
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(history);
        doThrow(failure).when(historiaExitoRepository).deleteById(2L);

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.eliminarHistoriaExito(2L, 1L));

        assertEquals("Error al eliminar la historia de éxito con ID: 2", exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void actualizarHistoriaExitoSavesAssociatedHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        HistoriaExitoEntity submitted = new HistoriaExitoEntity();
        submitted.setId(2L);
        HistoriaExitoEntity stored = new HistoriaExitoEntity();
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(stored);

        service.actualizarHistoriaExito(submitted, 1L);

        verify(historiaExitoRepository).save(stored);
    }

    @Test
    void actualizarHistoriaExitoRejectsMissingHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        HistoriaExitoEntity submitted = new HistoriaExitoEntity();
        submitted.setId(2L);
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(null);

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.actualizarHistoriaExito(submitted, 1L));

        assertEquals(
                "La historia de éxito con ID asociada a la mascota seleccionada: 2 no existe",
                exception.getMessage());
        verify(historiaExitoRepository, never()).save(any(HistoriaExitoEntity.class));
    }

    @Test
    void actualizarHistoriaExitoWrapsRepositoryFailure() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        HistoriaExitoEntity submitted = new HistoriaExitoEntity();
        submitted.setId(2L);
        HistoriaExitoEntity stored = new HistoriaExitoEntity();
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(stored);
        when(historiaExitoRepository.save(stored)).thenThrow(failure);

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.actualizarHistoriaExito(submitted, 1L));

        assertEquals(
                "Error al actualizar la historia de éxito con ID: 2 para la mascota con ID: 1",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void obtenerHistoriaExitoByIdAndIdMascotaReturnsHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        HistoriaExitoEntity expected = new HistoriaExitoEntity();
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(expected);

        assertEquals(expected, service.obtenerHistoriaExitoByIdAndIdMascota(2L, 1L));
    }

    @Test
    void obtenerHistoriaExitoByIdAndIdMascotaRejectsMissingHistory() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenReturn(null);

        assertThrows(HistoriaExitoService.HistoriaExitoException.class,
                () -> service.obtenerHistoriaExitoByIdAndIdMascota(2L, 1L));
    }

    @Test
    void obtenerHistoriaExitoByIdAndIdMascotaWrapsRepositoryFailure() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaIdAndId(1L, 2L)).thenThrow(failure);

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.obtenerHistoriaExitoByIdAndIdMascota(2L, 1L));

        assertEquals(
                "Error al obtener la historia de éxito con ID: 2 para la mascota con ID: 1",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }

    @Test
    void obtenerHistoriasExitoByIdMascotaReturnsHistories() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        List<HistoriaExitoEntity> expected = List.of(new HistoriaExitoEntity());
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaId(1L)).thenReturn(expected);

        assertEquals(expected, service.obtenerHistoriasExitoByIdMascota(1L));
    }

    @Test
    void obtenerHistoriasExitoByIdMascotaRejectsMissingMascota() {
        when(mascotaRepository.findById(1L)).thenReturn(Optional.empty());

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.obtenerHistoriasExitoByIdMascota(1L));

        assertEquals("La mascota con ID: 1 no existe", exception.getMessage());
        verifyNoInteractions(historiaExitoRepository);
    }

    @Test
    void obtenerHistoriasExitoByIdMascotaWrapsRepositoryFailure() {
        MascotaEntity mascota = new MascotaEntity();
        mascota.setEstado("Disponible");
        RuntimeException failure = new IllegalStateException("database failure");
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(historiaExitoRepository.findByMascotaId(1L)).thenThrow(failure);

        HistoriaExitoService.HistoriaExitoException exception = assertThrows(
                HistoriaExitoService.HistoriaExitoException.class,
                () -> service.obtenerHistoriasExitoByIdMascota(1L));

        assertEquals(
                "Error al obtener las historias de éxito para la mascota con ID: 1",
                exception.getMessage());
        assertSame(failure, exception.getCause());
    }
}