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

import co.edu.udistrital.mdp.pets.entities.SolicitudAdopcionEntity;
import co.edu.udistrital.mdp.pets.repositories.SolicitudAdopcionRepository;

@ExtendWith(MockitoExtension.class)
class SolicitudAdopcionServiceTest {

    private static final Long ID = 7L;

    @Mock
    private SolicitudAdopcionRepository solicitudRepository;

    @InjectMocks
    private SolicitudAdopcionService solicitudService;

    @Test
    void testCreateSolicitud() {
        SolicitudAdopcionEntity solicitud = new SolicitudAdopcionEntity();
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);

        assertSame(solicitud, solicitudService.createSolicitud(solicitud));

        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void testCreateSolicitudCuandoRepositorioFalla() {
        SolicitudAdopcionEntity solicitud = new SolicitudAdopcionEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(solicitudRepository.save(solicitud)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> solicitudService.createSolicitud(solicitud));

        assertEquals("Error al crear la solicitud de adopción.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }

    @Test
    void testGetSolicitudes() {
        List<SolicitudAdopcionEntity> solicitudes = List.of(new SolicitudAdopcionEntity());
        when(solicitudRepository.findAll()).thenReturn(solicitudes);

        assertSame(solicitudes, solicitudService.getSolicitudes());

        verify(solicitudRepository).findAll();
    }

    @Test
    void testGetSolicitudExistente() {
        SolicitudAdopcionEntity solicitud = new SolicitudAdopcionEntity();
        when(solicitudRepository.findById(ID)).thenReturn(Optional.of(solicitud));

        assertSame(solicitud, solicitudService.getSolicitud(ID));

        verify(solicitudRepository).findById(ID);
    }

    @Test
    void testGetSolicitudInexistente() {
        when(solicitudRepository.findById(ID)).thenReturn(Optional.empty());

        assertNull(solicitudService.getSolicitud(ID));

        verify(solicitudRepository).findById(ID);
    }

    @Test
    void testGetSolicitudesByEstado() {
        List<SolicitudAdopcionEntity> solicitudes = List.of(new SolicitudAdopcionEntity());
        when(solicitudRepository.findByEstado("APROBADA")).thenReturn(solicitudes);

        assertSame(solicitudes, solicitudService.getSolicitudesByEstado("APROBADA"));

        verify(solicitudRepository).findByEstado("APROBADA");
    }

    @Test
    void testGetSolicitudesByAdoptante() {
        List<SolicitudAdopcionEntity> solicitudes = List.of(new SolicitudAdopcionEntity());
        when(solicitudRepository.findByAdoptanteId(ID)).thenReturn(solicitudes);

        assertSame(solicitudes, solicitudService.getSolicitudesByAdoptante(ID));

        verify(solicitudRepository).findByAdoptanteId(ID);
    }

    @Test
    void testGetSolicitudesByMascota() {
        List<SolicitudAdopcionEntity> solicitudes = List.of(new SolicitudAdopcionEntity());
        when(solicitudRepository.findByMascotaId(ID)).thenReturn(solicitudes);

        assertSame(solicitudes, solicitudService.getSolicitudesByMascota(ID));

        verify(solicitudRepository).findByMascotaId(ID);
    }

    @Test
    void testUpdateSolicitudExistente() {
        SolicitudAdopcionEntity existente = new SolicitudAdopcionEntity();
        SolicitudAdopcionEntity actualizada = new SolicitudAdopcionEntity();
        when(solicitudRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(solicitudRepository.save(actualizada)).thenReturn(actualizada);

        SolicitudAdopcionEntity result = solicitudService.updateSolicitud(ID, actualizada);

        assertSame(actualizada, result);
        assertEquals(ID, actualizada.getId());
        verify(solicitudRepository).save(actualizada);
    }

    @Test
    void testUpdateSolicitudInexistente() {
        SolicitudAdopcionEntity solicitud = new SolicitudAdopcionEntity();
        when(solicitudRepository.findById(ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> solicitudService.updateSolicitud(ID, solicitud));

        assertEquals("Error al actualizar la solicitud de adopción.", exception.getMessage());
        assertEquals("La solicitud de adopción no existe.", exception.getCause().getMessage());
        verify(solicitudRepository).findById(ID);
        verifyNoMoreInteractions(solicitudRepository);
    }

    @Test
    void testUpdateSolicitudCuandoRepositorioFalla() {
        SolicitudAdopcionEntity existente = new SolicitudAdopcionEntity();
        SolicitudAdopcionEntity actualizada = new SolicitudAdopcionEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(solicitudRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(solicitudRepository.save(actualizada)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> solicitudService.updateSolicitud(ID, actualizada));

        assertEquals("Error al actualizar la solicitud de adopción.", exception.getMessage());
        assertSame(causa, exception.getCause());
        assertEquals(ID, actualizada.getId());
    }

    @Test
    void testDeleteSolicitud() {
        solicitudService.deleteSolicitud(ID);

        verify(solicitudRepository).deleteById(ID);
    }

    @Test
    void testDeleteSolicitudCuandoRepositorioFalla() {
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        doThrow(causa).when(solicitudRepository).deleteById(ID);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> solicitudService.deleteSolicitud(ID));

        assertEquals("Error al eliminar la solicitud de adopción.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }
}
