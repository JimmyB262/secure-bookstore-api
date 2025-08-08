package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.example.dto.BookAuthorDTO;
import org.example.entity.Author;
import org.example.entity.Book;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class AuthorInfoBean implements Serializable {

    private Author selectedAuthor;
    private List<BookAuthorDTO> matchingBooks;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                selectedAuthor = fetchAuthor(id);
                getAuthorBooks(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private Author fetchAuthor(int id) throws IOException {
        Client client = ClientBuilder.newClient();
        String jwt = getJwtFromCookie();
        Author author;
        if (jwt == null || jwt.isBlank()) {
            System.err.println("JWT token not found.");
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/Helloworld-1.0-SNAPSHOT/login.xhtml");
            return null;
        }
        String endpoint = "http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/author/" + id;

        Response response = client.target(endpoint)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
                .get();

        if (response.getStatus() == 200) {
            author = response.readEntity(Author.class);
        } else if (response.getStatus() == 401 || response.getStatus() == 403) {
            System.err.println("Unauthorized access: token may be expired or invalid.");
            author = new Author();
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("/Helloworld-1.0-SNAPSHOT/login.xhtml");

        } else {
            System.err.println("Unexpected error while fetching author: HTTP " + response.getStatus());
            author = new Author();
        }
        return author;
    }

    public String getAuthorBooks(int id){
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/Helloworld-1.0-SNAPSHOT/login.xhtml");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book/author/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                matchingBooks = response.readEntity(new GenericType<List<BookAuthorDTO>>() {});
            } else if (response.getStatus() == 401 || response.getStatus() == 403) {
                System.err.println("Unauthorized access: token may be expired or invalid.");
                matchingBooks = Collections.emptyList();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/Helloworld-1.0-SNAPSHOT/login.xhtml");

            } else {
                System.err.println("Unexpected error while loading authors: HTTP " + response.getStatus());
                matchingBooks = Collections.emptyList();
            }
        } catch (Exception e) {
            matchingBooks = List.of();
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
    public Author getSelectedAuthor() {
        return selectedAuthor;
    }

    public void setSelectedAuthor(Author selectedAuthor) {
        this.selectedAuthor = selectedAuthor;
    }

    public List<BookAuthorDTO> getMatchingBooks() {
        return matchingBooks;
    }

    public void setMatchingBooks(List<BookAuthorDTO> matchingBooks) {
        this.matchingBooks = matchingBooks;
    }
}

