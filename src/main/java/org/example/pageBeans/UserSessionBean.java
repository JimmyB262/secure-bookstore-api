package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletResponse;
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

    public String logout() {
        // Invalidate session
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().invalidateSession();

        // Delete specific cookie (e.g., "user" or your custom one)
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        facesContext.getExternalContext().getSession(true);

        // Redirect to login page
        return "/login.xhtml?faces-redirect=true";
    }

}



