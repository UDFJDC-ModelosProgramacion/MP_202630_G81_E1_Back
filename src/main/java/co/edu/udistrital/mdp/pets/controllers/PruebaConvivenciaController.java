package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.entities.PruebaConvivenciaEntity;
import co.edu.udistrital.mdp.pets.services.PruebaConvivenciaService;

@RestController
@RequestMapping("/pruebas-convivencia")
public class PruebaConvivenciaController {

    private final PruebaConvivenciaService pruebaService;

    public PruebaConvivenciaController(PruebaConvivenciaService pruebaService) {
        this.pruebaService = pruebaService;
    }

    @GetMapping
    public List<PruebaConvivenciaEntity> getPruebas() {
        return pruebaService.getPruebas();
    }

    @GetMapping("/{id}")
    public PruebaConvivenciaEntity getPrueba(@PathVariable Long id) {
        return pruebaService.getPrueba(id);
    }

    @GetMapping("/estado/{estado}")
    public List<PruebaConvivenciaEntity> getPruebasByEstado(@PathVariable String estado) {
        return pruebaService.getPruebasByEstado(estado);
    }

    @GetMapping("/adopcion/{adopcionId}")
    public PruebaConvivenciaEntity getPruebaByAdopcion(@PathVariable Long adopcionId) {
        return pruebaService.getPruebaByAdopcion(adopcionId);
    }

    @PostMapping
    public PruebaConvivenciaEntity createPrueba(@RequestBody PruebaConvivenciaEntity prueba) {
        return pruebaService.createPrueba(prueba);
    }

    @PutMapping("/{id}")
    public PruebaConvivenciaEntity updatePrueba(@PathVariable Long id,
            @RequestBody PruebaConvivenciaEntity prueba) {
        return pruebaService.updatePrueba(id, prueba);
    }

    @DeleteMapping("/{id}")
    public void deletePrueba(@PathVariable Long id) {
        pruebaService.deletePrueba(id);
    }
}