package org.example.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.example.util.JwtUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("JwtAuthFilter triggered for path: " + requestContext.getUriInfo().getPath());
        String token = null;
        String path = requestContext.getUriInfo().getPath();

        if (path.equals("/auth/login") || path.equals("auth/register")) {
            return;
        }

        //Try Authorization header
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring("Bearer ".length());
        }

        //If missing, try cookie
        if (token == null) {
            Cookie cookie = requestContext.getCookies().get("token");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        System.out.println("Extracted JWT token: " + (token == null ? "null" : token.substring(0, 10) + "..."));

        //If still missing, reject
        if (token == null) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Missing token" + path).build());
            return;
        }

        try {
            //Validate token using JwtUtil
            Jws<Claims> jwsClaims = JwtUtil.validateToken(token);
            Claims claims = jwsClaims.getBody();

            String username = claims.getSubject(); // sub claim
            List<String> roles = claims.get("groups", List.class); // roles from "groups"

            System.out.println("Token validated for user: " + username + ", roles: " + roles);

            //Set security context
            SecurityContext originalContext = requestContext.getSecurityContext();
            SecurityContext customContext = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return roles != null && roles.contains(role);
                }

                @Override
                public boolean isSecure() {
                    return originalContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            };
            System.out.println("Setting security context for user: " + username + ", roles: " + roles);

            requestContext.setSecurityContext(customContext);

        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Invalid or expired token").build());
        }
    }
}

