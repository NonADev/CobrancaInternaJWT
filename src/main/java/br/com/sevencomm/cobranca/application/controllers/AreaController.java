package br.com.sevencomm.cobranca.application.controllers;

import br.com.sevencomm.cobranca.domain.interfaces.IAreaService;
import br.com.sevencomm.cobranca.domain.models.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private IAreaService areaService;

    @GetMapping
    public ResponseEntity<?> getAllAreas() {
        try {
            return ResponseEntity.ok(areaService.listAreas());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAreaById(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(areaService.getAreaById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/users/{area_id}")
    public ResponseEntity<?> getByAreaId(@PathVariable("area_id") Integer id) {
        try {
            return ResponseEntity.ok(areaService.listUsersByArea(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new java.lang.Error(e.getMessage()));
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> getAreasByName(@PathVariable("nome") String nome) {
        try {
            return ResponseEntity.ok(areaService.listAreasByName(nome));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createArea(@RequestBody Area area) {
        try {
            return ResponseEntity.created(getURI(areaService.insertArea(area).getId())).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArea(@RequestBody Area area, @PathVariable("id") Integer id) {
        try {
            Area aux = areaService.updateArea(area, id);
            return aux != null ? ResponseEntity.ok(aux) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArea(@PathVariable("id") Integer id) {
        try {
            areaService.deleteArea(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    private URI getURI(Integer id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    private static class Error {
        public String error;

        public Error(String error) {
            this.error = error;
        }
    }
}
