package com.example.keycloakadmin.repository;

import com.example.keycloakadmin.model.User;

import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    List<User> findByUserName(String userName);
    Optional<User> findById(String id);
    Optional<User> createUser(User user);
    Response deleteUserById(String id);
}
