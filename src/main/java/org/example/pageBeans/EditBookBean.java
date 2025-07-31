package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Map;

import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.BookAuthorDTO;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;

@Named
@ViewScoped
public class EditBookBean implements Serializable {

    private BookAuthorDTO book;
    private Integer id;

    // Inject your AuthorService (replace this with your actual service)
    @Inject
    private BookRepository bookService;

    @PostConstruct
    public void init() {
        // Get ID from URL
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (param != null) {
            try {
                id = Integer.parseInt(param);
                book = bookService.getBookById(id);
            } catch (NumberFormatException e) {
                // handle invalid ID
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid book ID"));
            }
        }
    }

    public String saveBook() {
        try {
            boolean success = updateBookViaApi(book);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Book updated successfully."));
                return "books?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update book.", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error occurred.", null));
        }
        return null;
    }

    private boolean updateBookViaApi(BookAuthorDTO updatedBook) {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return false;
            }

            if (updatedBook == null || updatedBook.getId() == null) {
                System.err.println("Book or Book ID is null.");
                return false;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .put(Entity.entity(updatedBook, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                System.out.println("Book updated successfully.");
                return true;
            } else if (response.getStatus() == 404) {
                System.err.println("Book not found.");
            } else {
                System.err.println("Failed to update Book: " + response.getStatus());
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

    // Getters & Setters
    public BookAuthorDTO getBook() {
        return book;
    }

    public void setBook(BookAuthorDTO book) {
        this.book = book;
    }
}