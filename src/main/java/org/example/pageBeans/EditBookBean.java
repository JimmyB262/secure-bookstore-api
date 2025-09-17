package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
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
import org.example.dto.BookAuthorDTO;
import org.example.dto.BookStockDTO;
import org.example.repository.BookRepository;
import org.example.repository.StockRepository;

@Named
@ViewScoped
public class EditBookBean implements Serializable {

    private BookAuthorDTO book;
    private Integer id;
    private BookStockDTO stock;


    @EJB
    private BookRepository bookRepository;

    @EJB
    private StockRepository stockRepository;

    @PostConstruct
    public void init() {
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (param != null) {
            try {
                id = Integer.parseInt(param);
                book = bookRepository.getBookById(id);
                stock = stockRepository.getStockById(id);

            } catch (Exception e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to load book data: " + e.getMessage(), null));
            }
        }
    }


    public String saveBook() {
        try {
            boolean successBookSave = updateBookViaApi(book);
            boolean successStockSave = updateStockViaApi(stock);
            if (successBookSave & successStockSave ) {
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

    private boolean updateStockViaApi(BookStockDTO updatedStock) {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return false;
            }

            if (updatedStock == null || updatedStock.getId() == null) {
                System.err.println("Stock or Stock ID is null.");
                return false;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .put(Entity.entity(updatedStock, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                System.out.println("Stock updated successfully.");
                return true;
            } else if (response.getStatus() == 404) {
                System.err.println("Stock not found.");
            } else {
                System.err.println("Failed to update Stock: " + response.getStatus());
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


    public BookAuthorDTO getBook() {
        return book;
    }

    public void setBook(BookAuthorDTO book) {
        this.book = book;
    }

    public BookStockDTO getStock() {
        return stock;
    }

    public void setStock(BookStockDTO stock) {
        this.stock = stock;
    }
}