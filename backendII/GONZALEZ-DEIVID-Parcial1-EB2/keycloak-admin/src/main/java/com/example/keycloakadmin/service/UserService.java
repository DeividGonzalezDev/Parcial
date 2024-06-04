package com.example.keycloakadmin.service;

import com.example.keycloakadmin.model.User;
import com.example.keycloakadmin.repository.IUserRepository;

import jakarta.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public List<User> findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    public Optional<User> createUser(User user) {
        return repository.createUser(user);
    }

    public Response deleteUser(String id){
        return repository.deleteUserById(id);
    }

}
