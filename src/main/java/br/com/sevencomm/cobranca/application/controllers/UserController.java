package br.com.sevencomm.cobranca.application.controllers;

import br.com.sevencomm.cobranca.domain.interfaces.IUserService;
import br.com.sevencomm.cobranca.domain.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> listUsers() {
        try {
            return ResponseEntity.ok(userService.listUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestParam Integer userId) {
        try {
            return ResponseEntity.ok(userService.getUser(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-current-user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            return ResponseEntity.ok(userService.getCurrentUser());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/area/{area_id}")
    public ResponseEntity<?> getByAreaId(@PathVariable("area_id") Integer id) {
        try {
            return ResponseEntity.ok(userService.listUsersByAreaId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new java.lang.Error(e.getMessage()));
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> insertUser(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(userService.insertUser(usuario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
