package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;

@Named
@ViewScoped
public class EditAuthorBean implements Serializable {

    private Author author;
    private Integer id;


    @Inject
    private AuthorRepository authorRepository;

    @PostConstruct
    public void init() {
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (param != null) {
            try {
                id = Integer.parseInt(param);
                author = authorRepository.getAuthorById(id);
            } catch (NumberFormatException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid author ID"));
            }
        }
    }

    public String saveAuthor() {
        try {
            boolean success = updateAuthorViaApi(author);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Author updated successfully."));
                return "authors?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update author.", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error occurred.", null));
        }
        return null;
    }

    private boolean updateAuthorViaApi(Author updatedAuthor) {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return false;
            }

            if (updatedAuthor == null || updatedAuthor.getAuthor_id() == null) {
                System.err.println("Author or Author ID is null.");
                return false;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .put(Entity.entity(updatedAuthor, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                System.out.println("Author updated successfully.");
                return true;
            } else if (response.getStatus() == 404) {
                System.err.println("Author not found.");
            } else {
                System.err.println("Failed to update author: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        return false;
    }

    public String getJwtFromCookie() {
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}

