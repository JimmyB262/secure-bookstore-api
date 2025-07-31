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
import org.example.dto.BookStockDTO;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.StockRepository;

@Named
@ViewScoped
public class EditStockBean implements Serializable {

    private BookStockDTO stock;
    private Integer id;

    @Inject
    private StockRepository stockService;

    @PostConstruct
    public void init() {
        String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (param != null) {
            try {
                id = Integer.parseInt(param);
                stock = stockService.getStockById(id);
            } catch (NumberFormatException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid stock ID"));
            }
        }
    }

    public String saveStock() {
        try {
            boolean success = updateStockViaApi(stock);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Stock updated successfully."));
                return "stock?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update stock.", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error occurred.", null));
        }
        return null;
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

    public BookStockDTO getStock() {
        return stock;
    }

    public void setStock(BookStockDTO stock) {
        this.stock = stock;
    }
}
