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
import org.example.dto.BookAuthorDTO;


import java.util.Collections;
import java.util.List;

@Named
@RequestScoped
public class BookBean {

    private List<BookAuthorDTO> books;
    private BookAuthorDTO bookById;
    private Integer searchBookId;

    @PostConstruct
    public void init() {
        loadAllBooks();
    }

    public void loadAllBooks() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                books = Collections.emptyList();
                return;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                books = response.readEntity(new GenericType<List<BookAuthorDTO>>() {});
            } else {
                books = Collections.emptyList();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("/Helloworld-1.0-SNAPSHOT/login.xhtml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            books = Collections.emptyList();
        } finally {
            client.close();
        }
    }

    public void loadBookById() {
        if (searchBookId == null) {
            bookById = null;
            return;
        }

        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                bookById = null;
                return;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book/" + searchBookId)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get();

            if (response.getStatus() == 200) {
                bookById = response.readEntity(BookAuthorDTO.class);
            } else {
                bookById = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bookById = null;
        } finally {
            client.close();
        }
    }

    public String getJwtFromCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) return null;

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

    // Getters and setters

    public List<BookAuthorDTO> getBooks() {
        return books;
    }

    public BookAuthorDTO getBookById() {
        return bookById;
    }

    public Integer getSearchBookId() {
        return searchBookId;
    }

    public void setSearchBookId(Integer searchBookId) {
        this.searchBookId = searchBookId;
    }
}
