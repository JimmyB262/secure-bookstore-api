package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.example.util.JwtUtil;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {

    private String username;
    private List<String> roles;

    public String getUsername() {
        ensureUserFromToken(); // refresh every time
        return username;
    }

    public List<String> getRoles() {
        ensureUserFromToken();
        return roles;
    }

    public boolean isAdmin() {
        ensureUserFromToken();
        return roles != null && roles.contains("admin");
    }

    private void ensureUserFromToken() {
        try {
            String jwt = getJwtFromCookie();
            if (jwt != null) {
                Jws<Claims> jwsClaims = JwtUtil.validateToken(jwt);
                Claims claims = jwsClaims.getBody();

                this.username = claims.getSubject();
                this.roles = claims.get("groups", List.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.username = null;
            this.roles = null;
        }
    }

    private String getJwtFromCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}



