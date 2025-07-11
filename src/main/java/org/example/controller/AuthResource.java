package org.example.controller;


import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.LoginRequestDTO;
import org.example.entity.User;
import org.example.util.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.mindrot.jbcrypt.BCrypt;


import java.util.List;
import java.util.Map;


@Path("/auth")
public class AuthResource {


    @PersistenceContext
    private EntityManager em;

    @PermitAll
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        User user = em.find(User.class, username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            List<String> roles = List.of(user.getRoles().split("\\s*,\\s*"));
            String token = JwtUtil.generateToken(username, roles);
            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}