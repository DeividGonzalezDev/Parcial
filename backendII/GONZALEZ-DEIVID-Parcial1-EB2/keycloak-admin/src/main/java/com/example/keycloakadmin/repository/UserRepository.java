package com.example.keycloakadmin.repository;

import com.example.keycloakadmin.model.User;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

    private final Keycloak keycloakClient;

    @Value("${dh.keycloak.realm}")
    private String realm;

    /**
     * Retrieves a list of all users from the Keycloak server.
     *
     * @return          a list of User objects representing all the users
     */
    public List<User> findAll() {
        return keycloakClient.realm(realm).users().list().stream()
                .map(this::toUser).collect(Collectors.toList());
    }

    
    /**
     * Converts a UserRepresentation object to a User object.
     *
     * @param  userRepresentation  the UserRepresentation object to convert
     * @return                    the converted User object
     */
    private User toUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .userName(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }

    /**
     * Finds a list of users by their username.
     *
     * @param  userName  the username to search for
     * @return           a list of User objects that match the given username
     */
    public List<User> findByUserName(String userName) {
        List<UserRepresentation> userRepresentation = keycloakClient
                .realm(realm)
                .users()
                .search(userName);
        return userRepresentation.stream().map(this::toUser)
                .collect(Collectors.toList());
    }

    /**
     * Finds a user by their ID.
     *
     * @param  id  the ID of the user to find
     * @return     an optional containing the found user, or an empty optional if the user is not found
     */
    public Optional<User> findById(String id) {
        try {
            UserRepresentation userRepresentation = keycloakClient.realm(realm).users().get(id).toRepresentation();
            return Optional.ofNullable(toUser(userRepresentation));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    
    /**
     * Creates a new user in the system based on the provided User object.
     *
     * @param  user  the User object containing the user's information
     * @return       an Optional containing the created User object if successful,
     *               otherwise an empty Optional
     */
    public Optional<User> createUser(User user) {
        UserRepresentation userToUserRepresentation = new UserRepresentation();
        userToUserRepresentation.setUsername(user.getUserName());
        userToUserRepresentation.setEmail(user.getEmail());
        userToUserRepresentation.setFirstName(user.getFirstName());
        userToUserRepresentation.setLastName(user.getLastName());
        userToUserRepresentation.setEnabled(true);
        try {
            Response res = keycloakClient
                    .realm(realm)
                    .users()
                    .create(userToUserRepresentation);

            if (res.getStatus() != 201) {
                return Optional.empty();
            }
            return Optional.ofNullable(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    /**
     * Deletes a user by their ID.
     *
     * @param  id  the ID of the user to delete
     * @return     a Response object indicating the status of the deletion operation
     */
    @Override
    public Response deleteUserById(String id) {
        Optional<User> user = this.findById(id);
        if (!user.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            Response response = keycloakClient.realm(realm).users().delete(id);
            return Response.status(response.getStatus())
                    .entity(user.get())
                    .build();
        }

    }
}
