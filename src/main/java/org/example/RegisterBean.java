package org.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.example.entity.User;
import org.mindrot.jbcrypt.BCrypt;

@Named
@RequestScoped
public class RegisterBean {

    private String username;
    private String password;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public String register() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (username != null) {
            username = username.trim();
        }
        if (password != null) {
            password = password.trim();
        }


        if (username == null || username.length() < 3 || username.length() > 20) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Username must be between 3 and 20 characters", null));
            return null;
        }


        if (password == null || password.length() < 6 || password.length() > 30) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Password must be between 6 and 30 characters", null));
            return null;
        }


        String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-={};:'\"|,.<>?]).{6,30}$";
        if (!password.matches(passwordPattern)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Password must contain at least one uppercase letter, one number, and one special character", null));
            return null;
        }

        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)", User.class);
        query.setParameter("username", username.toLowerCase());

        if (!query.getResultList().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already exists", null));
            return null;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRoles("user");

        em.persist(user);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "User registered successfully", null));

        return null;
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

