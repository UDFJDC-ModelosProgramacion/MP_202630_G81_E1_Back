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

import co.edu.udistrital.mdp.pets.entities.RetornoEntity;
import co.edu.udistrital.mdp.pets.repositories.RetornoRepository;

@ExtendWith(MockitoExtension.class)
class RetornoServiceTest {

    private static final Long ID = 7L;

    @Mock
    private RetornoRepository retornoRepository;

    @InjectMocks
    private RetornoService retornoService;

    @Test
    void testCreateRetorno() {
        RetornoEntity retorno = new RetornoEntity();
        when(retornoRepository.save(retorno)).thenReturn(retorno);

        assertSame(retorno, retornoService.createRetorno(retorno));

        verify(retornoRepository).save(retorno);
    }

    @Test
    void testCreateRetornoCuandoRepositorioFalla() {
        RetornoEntity retorno = new RetornoEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(retornoRepository.save(retorno)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> retornoService.createRetorno(retorno));

        assertEquals("Error al registrar el retorno.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }

    @Test
    void testGetRetornos() {
        List<RetornoEntity> retornos = List.of(new RetornoEntity());
        when(retornoRepository.findAll()).thenReturn(retornos);

        assertSame(retornos, retornoService.getRetornos());

        verify(retornoRepository).findAll();
    }

    @Test
    void testGetRetornoExistente() {
        RetornoEntity retorno = new RetornoEntity();
        when(retornoRepository.findById(ID)).thenReturn(Optional.of(retorno));

        assertSame(retorno, retornoService.getRetorno(ID));

        verify(retornoRepository).findById(ID);
    }

    @Test
    void testGetRetornoInexistente() {
        when(retornoRepository.findById(ID)).thenReturn(Optional.empty());

        assertNull(retornoService.getRetorno(ID));

        verify(retornoRepository).findById(ID);
    }

    @Test
    void testGetRetornosCompatibles() {
        List<RetornoEntity> retornos = List.of(new RetornoEntity());
        when(retornoRepository.findByCompatibleReAdopcion(true)).thenReturn(retornos);

        assertSame(retornos, retornoService.getRetornosCompatibles(true));

        verify(retornoRepository).findByCompatibleReAdopcion(true);
    }

    @Test
    void testGetRetornoByAdopcionExistente() {
        RetornoEntity retorno = new RetornoEntity();
        when(retornoRepository.findByAdopcionId(ID)).thenReturn(Optional.of(retorno));

        assertSame(retorno, retornoService.getRetornoByAdopcion(ID));

        verify(retornoRepository).findByAdopcionId(ID);
    }

    @Test
    void testGetRetornoByAdopcionInexistente() {
        when(retornoRepository.findByAdopcionId(ID)).thenReturn(Optional.empty());

        assertNull(retornoService.getRetornoByAdopcion(ID));

        verify(retornoRepository).findByAdopcionId(ID);
    }

    @Test
    void testUpdateRetornoExistente() {
        RetornoEntity existente = new RetornoEntity();
        RetornoEntity actualizado = new RetornoEntity();
        when(retornoRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(retornoRepository.save(actualizado)).thenReturn(actualizado);

        RetornoEntity result = retornoService.updateRetorno(ID, actualizado);

        assertSame(actualizado, result);
        assertEquals(ID, actualizado.getId());
        verify(retornoRepository).save(actualizado);
    }

    @Test
    void testUpdateRetornoInexistente() {
        RetornoEntity retorno = new RetornoEntity();
        when(retornoRepository.findById(ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> retornoService.updateRetorno(ID, retorno));

        assertEquals("Error al actualizar el retorno.", exception.getMessage());
        assertEquals("El retorno no existe.", exception.getCause().getMessage());
        verify(retornoRepository).findById(ID);
        verifyNoMoreInteractions(retornoRepository);
    }

    @Test
    void testUpdateRetornoCuandoRepositorioFalla() {
        RetornoEntity existente = new RetornoEntity();
        RetornoEntity actualizado = new RetornoEntity();
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        when(retornoRepository.findById(ID)).thenReturn(Optional.of(existente));
        when(retornoRepository.save(actualizado)).thenThrow(causa);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> retornoService.updateRetorno(ID, actualizado));

        assertEquals("Error al actualizar el retorno.", exception.getMessage());
        assertSame(causa, exception.getCause());
        assertEquals(ID, actualizado.getId());
    }

    @Test
    void testDeleteRetorno() {
        retornoService.deleteRetorno(ID);

        verify(retornoRepository).deleteById(ID);
    }

    @Test
    void testDeleteRetornoCuandoRepositorioFalla() {
        RuntimeException causa = new RuntimeException("fallo de persistencia");
        doThrow(causa).when(retornoRepository).deleteById(ID);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class, () -> retornoService.deleteRetorno(ID));

        assertEquals("Error al eliminar el retorno.", exception.getMessage());
        assertSame(causa, exception.getCause());
    }
}
