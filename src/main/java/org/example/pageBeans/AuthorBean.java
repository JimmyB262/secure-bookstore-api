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
import jakarta.ws.rs.client.Entity;
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

    private Author newAuthor = new Author();

    public Author getNewAuthor() {
        return newAuthor;
    }

    public void setNewAuthor(Author newAuthor) {
        this.newAuthor = newAuthor;
    }

    private String authorSearchTerm;
    private List<Author> matchingAuthors;


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

    public String addAuthor() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .post(Entity.entity(newAuthor, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 201) {
                System.out.println("Author added successfully.");
                loadAllAuthors();
                newAuthor = new Author();
            } else {
                System.err.println("Failed to add author: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        loadAllAuthors();
        return null;
    }


    public String deleteAuthorById(Integer authorId) {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (authorId == null) {
                System.err.println("Author ID is null.");
                 return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author/" + authorId)
                    .request()
                    .header("Authorization", "Bearer " + jwt)
                    .delete();

            if (response.getStatus() == 200) {
                System.out.println("Author deleted successfully.");
            } else {
                System.err.println("Failed to delete author: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        loadAllAuthors();
        return "authors.xhtml?faces-redirect=true";
    }

    public String updateAuthor() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (newAuthor == null || newAuthor.getAuthor_id() == null) {
                System.err.println("Author or Author ID is null.");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .put(Entity.entity(newAuthor, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                System.out.println("Author updated successfully.");
                loadAllAuthors();
                newAuthor = new Author();
                return "index?faces-redirect=true";
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

        return null;
    }

    public String searchAuthor(){
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            authorSearchTerm = authorSearchTerm.trim();

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author/searchAuthorName/" + authorSearchTerm)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                matchingAuthors = response.readEntity(new GenericType<List<Author>>() {});
            } else {
                System.err.println("Author search failed: " + response.getStatus());
                matchingAuthors = List.of();
            }
        } catch (Exception e) {
            matchingAuthors = List.of();
        } finally {
            client.close();
        }

        return null;
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

    public void setAuthorSearchTerm(String authorSearchTerm) {
        this.authorSearchTerm = authorSearchTerm;
    }

    public List<Author> getMatchingAuthors() {
        return matchingAuthors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setAuthorById(Author authorById) {
        this.authorById = authorById;
    }

    public void setAuthorByBookId(Author authorByBookId) {
        this.authorByBookId = authorByBookId;
    }

    public String getAuthorSearchTerm() {
        return authorSearchTerm;
    }

    public void setMatchingAuthors(List<Author> matchingAuthors) {
        this.matchingAuthors = matchingAuthors;
    }
}
