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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class BookInfoBean implements Serializable {

    private BookAuthorDTO selectedBook;
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
                selectedBook = fetchBook(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private BookAuthorDTO fetchBook(int id) {
        Client client = ClientBuilder.newClient();
        String jwt = getJwtFromCookie();
        if (jwt == null || jwt.isBlank()) {
            System.err.println("JWT token not found.");
            return null;
        }
        String endpoint = "http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book/" + id;
        return client.target(endpoint)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
                .get(BookAuthorDTO.class);
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
    public BookAuthorDTO getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(BookAuthorDTO selectedBook) {
        this.selectedBook = selectedBook;
    }

    public List<BookAuthorDTO> getMatchingBooks() {
        return matchingBooks;
    }

    public void setMatchingBooks(List<BookAuthorDTO> matchingBooks) {
        this.matchingBooks = matchingBooks;
    }
}
