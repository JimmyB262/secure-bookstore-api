package org.example;


import io.jsonwebtoken.io.IOException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.example.entity.User;
import org.example.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Objects;

@Named
@RequestScoped
public class LoginBean {

    private String username;
    private String password;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public String login() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (username != null) {
            username = username.trim();
        }
        if (password != null) {
            password = password.trim();
        }

        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);

        if (!query.getResultList().isEmpty()) {

            User user = query.getSingleResult();

            if (BCrypt.checkpw(password, user.getPassword())){


                List<String> roles = List.of(user.getRoles().split("\\s*,\\s*"));
                String token = JwtUtil.generateToken(user.getUsername(), roles);


                HttpServletResponse response = (HttpServletResponse)
                        FacesContext.getCurrentInstance().getExternalContext().getResponse();

                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(3600);
                response.addCookie(cookie);

                try {
                    context.getExternalContext().redirect("/Helloworld-1.0-SNAPSHOT/api/author");
                    context.responseComplete();
                    return null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    context.addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Redirect failed", null));
                    return null;
                }

            }else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password", null));
                return null;
            }

        }else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No User Found With This Username", null));
            return null;
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
