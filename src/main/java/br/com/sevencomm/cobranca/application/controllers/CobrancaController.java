package br.com.sevencomm.cobranca.application.controllers;

import br.com.sevencomm.cobranca.domain.interfaces.ICobrancaService;
import br.com.sevencomm.cobranca.domain.models.Cobranca;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/cobranca-interna")
public class CobrancaController {

    @Autowired
    private ICobrancaService cobrancaInternaService;

    @GetMapping
    public ResponseEntity<?> get() {
        try {
            return ResponseEntity.ok(cobrancaInternaService.listCobrancas());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCobrancaById(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(cobrancaInternaService.getCobrancaById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/aprovar/{id}")
    public ResponseEntity<?> aprovarCobranca(@PathVariable("id") Integer id) {
        try {
            cobrancaInternaService.aprovarCobranca(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/recusar/{id}")
    public ResponseEntity<?> recusarCobranca(@PathVariable("id") Integer id) {
        try {
            cobrancaInternaService.recusarCobranca(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/get-cobrancas")
    public ResponseEntity<?> getCobrancas(@RequestParam("userId") Integer usuarioId) {
        try{
            return ResponseEntity.ok(cobrancaInternaService.listCobrancas(usuarioId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/get-cobrancas-recebidas")
    public ResponseEntity<?> getCobrancasRecebidas() {
        try {
            return ResponseEntity.ok(cobrancaInternaService.listCobrancasRecebidas());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/get-cobrancas-enviadas")
    public ResponseEntity<?> getCobrancasEnviadas() {
        try {
            return ResponseEntity.ok(cobrancaInternaService.listCobrancasEnviadas());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/area")
    public ResponseEntity<?> getCobrancasByArea(@RequestParam("areaId") Integer area) {
        try {
            return ResponseEntity.ok(cobrancaInternaService.listCobrancas(area));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getByStatusId(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(cobrancaInternaService.listAllByStatus(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Cobranca cobrancaInterna) {
        try {
            return ResponseEntity.created(getURI(cobrancaInternaService.insertCobranca(cobrancaInterna).getId())).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody Cobranca cobrancaInterna, @PathVariable("id") Integer id) {
        try {
            Cobranca aux = cobrancaInternaService.updateCobranca(cobrancaInterna, id);
            return ResponseEntity.ok(aux);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            cobrancaInternaService.deleteCobranca(id);
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
