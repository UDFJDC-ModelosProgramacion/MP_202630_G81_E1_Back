package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.entities.RetornoEntity;
import co.edu.udistrital.mdp.pets.services.RetornoService;

@RestController
@RequestMapping("/retornos")
public class RetornoController {

    private final RetornoService retornoService;

    public RetornoController(RetornoService retornoService) {
        this.retornoService = retornoService;
    }

    @GetMapping
    public List<RetornoEntity> getRetornos() {
        return retornoService.getRetornos();
    }

    @GetMapping("/{id}")
    public RetornoEntity getRetorno(@PathVariable Long id) {
        return retornoService.getRetorno(id);
    }

    @GetMapping("/compatible/{compatible}")
    public List<RetornoEntity> getRetornosCompatibles(@PathVariable Boolean compatible) {
        return retornoService.getRetornosCompatibles(compatible);
    }

    @GetMapping("/adopcion/{adopcionId}")
    public RetornoEntity getRetornoByAdopcion(@PathVariable Long adopcionId) {
        return retornoService.getRetornoByAdopcion(adopcionId);
    }

    @PostMapping
    public RetornoEntity createRetorno(@RequestBody RetornoEntity retorno) {
        return retornoService.createRetorno(retorno);
    }

    @PutMapping("/{id}")
    public RetornoEntity updateRetorno(@PathVariable Long id,
            @RequestBody RetornoEntity retorno) {
        return retornoService.updateRetorno(id, retorno);
    }

    @DeleteMapping("/{id}")
    public void deleteRetorno(@PathVariable Long id) {
        retornoService.deleteRetorno(id);
    }
}