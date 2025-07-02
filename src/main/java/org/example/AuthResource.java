package org.example;


import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.util.JwtUtil;

import java.util.List;
import java.util.Map;


@Path("/auth")
public class AuthResource {

    @PermitAll
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        if ("admin".equals(username) && "password".equals(password)) {
            String token = JwtUtil.generateToken(username, List.of("user"));
            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
