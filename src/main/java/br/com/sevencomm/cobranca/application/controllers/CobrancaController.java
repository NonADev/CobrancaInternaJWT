package br.com.sevencomm.cobranca.application.controllers;

import br.com.sevencomm.cobranca.domain.interfaces.ICobrancaService;
import br.com.sevencomm.cobranca.domain.interfaces.IComentarioService;
import br.com.sevencomm.cobranca.domain.models.Cobranca;
import br.com.sevencomm.cobranca.domain.models.Comentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.net.URI;


@RestController
@RequestMapping("/cobranca-interna")
public class CobrancaController {

    @Autowired
    private ICobrancaService cobrancaInternaService;

    @Autowired
    private IComentarioService comentarioService;

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
        try {
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

    @GetMapping("/comentario")
    public ResponseEntity<?> listComentariosByUser() {
        try {
            return ResponseEntity.ok(comentarioService.listComentariosByUsuarioId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/get-comentarios-by-cobranca")
    public ResponseEntity<?> listComentariosByCobranca(@RequestParam("cobrancaId") Integer id) {
        try {
            return ResponseEntity.ok(comentarioService.listComentariosByCobrancaId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @GetMapping("/get-comentario-by-id")
    public ResponseEntity<?> getComentarioById(@RequestParam("comentarioId") Integer id) {
        try {
            return ResponseEntity.ok(comentarioService.getComentarioById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PostMapping("/create-comentario")
    public ResponseEntity<?> postComentario(@RequestBody Comentario comentario) {
        try {
            return ResponseEntity.created(getURI("/cobranca-interna/get-comentarios-by-id?comentarioId="+comentarioService.insertComentario(comentario).getId())).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Error(e.getMessage()));
        }
    }

    @PutMapping("/update-comentario/{id}")
    public ResponseEntity<?> putComentario(@RequestBody Comentario comentario, @PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(comentarioService.updateComentario(comentario, id).getId());
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

    @DeleteMapping("/comentario/{id}")
    public ResponseEntity<?> deleteComentario(@PathVariable("id") Integer id) {
        try {
            comentarioService.deleteComentario(id);
            return ResponseEntity.ok().build();
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

    private URI getURI(String path) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("{path}")
                .buildAndExpand(path).toUri();
    }

    private static class Error {
        public String error;

        public Error(String error) {
            this.error = error;
        }
    }
}
