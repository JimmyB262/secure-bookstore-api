package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.BookStockDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import java.util.List;

import java.util.List;


@Named
@RequestScoped
public class StockBean {

    private List<BookStockDTO> stockList;
    private List<BookStockDTO> maxStock;
    private List<BookStockDTO> minStock;
    private List<BookStockDTO> sortedStock;
    private BookStockDTO stockById;
    private Integer bookIdToSearch;
    private Integer authorIdToSearch;
    private Integer authorBookSum;
    private Double avgStock;

    @PostConstruct
    public void init() {
        loadAll();
    }

    public void loadAll() {
        Client client = ClientBuilder.newClient();
        String jwt = getJwtFromCookie(); // You need to implement this method
        WebTarget base = client.target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock");

        try {
            stockList = base
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(new GenericType<List<BookStockDTO>>() {});

            maxStock = base.path("max")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(new GenericType<List<BookStockDTO>>() {});

            minStock = base.path("min")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(new GenericType<List<BookStockDTO>>() {});

            sortedStock = base.path("sortedlist")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(new GenericType<List<BookStockDTO>>() {});

            avgStock = base.path("avg")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(Double.class);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    public void searchByBookId() {
        Client client = ClientBuilder.newClient();
        String jwt = getJwtFromCookie();
        try {
            stockById = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock/" + bookIdToSearch)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(BookStockDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    public void searchAuthorBookSum() {
        Client client = ClientBuilder.newClient();
        String jwt = getJwtFromCookie();
        try {
            authorBookSum = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock/authorsum/" + authorIdToSearch)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .get(Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    public List<BookStockDTO> getStockList() {
        return stockList;
    }

    public List<BookStockDTO> getMaxStock() {
        return maxStock;
    }

    public List<BookStockDTO> getMinStock() {
        return minStock;
    }

    public List<BookStockDTO> getSortedStock() {
        return sortedStock;
    }

    public BookStockDTO getStockById() {
        return stockById;
    }

    public Integer getBookIdToSearch() {
        return bookIdToSearch;
    }

    public void setBookIdToSearch(Integer bookIdToSearch) {
        this.bookIdToSearch = bookIdToSearch;
    }

    public Integer getAuthorIdToSearch() {
        return authorIdToSearch;
    }

    public void setAuthorIdToSearch(Integer authorIdToSearch) {
        this.authorIdToSearch = authorIdToSearch;
    }

    public Integer getAuthorBookSum() {
        return authorBookSum;
    }

    public Double getAvgStock() {
        return avgStock;
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
}
