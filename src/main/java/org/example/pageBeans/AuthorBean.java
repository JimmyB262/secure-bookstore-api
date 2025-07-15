package org.example.pageBeans;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.entity.Author;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;


import java.util.Collections;
import java.util.List;

@Named
@RequestScoped
public class AuthorBean {

    private List<Author> authors;
    private Author authorById;
    private Author authorByBookId;

    private Integer searchAuthorId;
    private Integer searchBookId;

    @PostConstruct
    public void init() {
        loadAllAuthors();
    }

    public void loadAllAuthors() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                authors = Collections.emptyList();
                return;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                authors = response.readEntity(new GenericType<List<Author>>() {});
            } else if (response.getStatus() == 401 || response.getStatus() == 403) {
                System.err.println("Unauthorized access: token may be expired or invalid.");
                authors = Collections.emptyList();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/Helloworld-1.0-SNAPSHOT/login.xhtml");

            } else {
                System.err.println("Unexpected error while loading authors: HTTP " + response.getStatus());
                authors = Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            authors = Collections.emptyList();
        } finally {
            client.close();
        }
    }


    public void loadAuthorById() {
        if (searchAuthorId == null) {
            authorById = null;
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                authorById = null;
                return;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author/" + searchAuthorId)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                authorById = response.readEntity(Author.class);
            } else {
                System.err.println("Author not found or unauthorized (status: " + response.getStatus() + ")");
                authorById = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            authorById = null;
        } finally {
            client.close();
        }
    }


    public void loadAuthorByBookId() {
        if (searchBookId == null) {
            authorByBookId = null;
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                authorByBookId = null;
                return;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author/findByBook/" + searchBookId)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                authorByBookId = response.readEntity(Author.class);
            } else {
                System.err.println("Author not found for book or unauthorized (status: " + response.getStatus() + ")");
                authorByBookId = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            authorByBookId = null;
        } finally {
            client.close();
        }
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


    public List<Author> getAuthors() {
        return authors;
    }

    public Author getAuthorById() {
        return authorById;
    }

    public Author getAuthorByBookId() {
        return authorByBookId;
    }

    public Integer getSearchAuthorId() {
        return searchAuthorId;
    }

    public void setSearchAuthorId(Integer searchAuthorId) {
        this.searchAuthorId = searchAuthorId;
    }

    public Integer getSearchBookId() {
        return searchBookId;
    }

    public void setSearchBookId(Integer searchBookId) {
        this.searchBookId = searchBookId;
    }
}
