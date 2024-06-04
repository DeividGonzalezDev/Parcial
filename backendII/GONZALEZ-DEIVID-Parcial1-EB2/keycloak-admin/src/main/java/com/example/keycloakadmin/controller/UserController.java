package com.example.keycloakadmin.controller;


import com.example.keycloakadmin.model.User;
import com.example.keycloakadmin.service.UserService;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/username/{userName}")
    public ResponseEntity<List<User>> findByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(service.findByUserName(userName));
    }

    //Cree este metodo con un Get para poder probar la funcionalidad de crear usuarios, desde el navegador, se debería usar @PostMapping
    //url tipo: ?username=user-from-spring&email=user-from-spring@gmail.com&firstName=User&lastName=Spring
    @GetMapping("/create")
    public ResponseEntity<User> createUser(@PathParam("username") String username, @PathParam("email") String email, @PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        Optional<User> userSaved = service.createUser(User.builder().userName(username).email(email).firstName(firstName).lastName(lastName).build());
        if (userSaved.isPresent()) {
            return ResponseEntity.ok(userSaved.get());
        } else {
            return ResponseEntity.badRequest().body(User.builder().build());
        }
    }

    //Cree este metodo con un Get para poder probar la funcionalidad de eliminar usuarios, desde el navegador, se debería usar @DeleteMapping
   @GetMapping("/delete/{id}")
   public Response deleteUser(@PathVariable String id) {
       return service.deleteUser(id);
   } 

}
