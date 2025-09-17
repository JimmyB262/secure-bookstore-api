package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.BookAuthorDTO;
import org.example.dto.BookStockDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;

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
    private Integer selectedBookId;
    private Integer authorIdToSearch;
    private Integer authorBookSum;
    private Double avgStock;
    private String authorSearchTerm;
    private List<Author> authors;


    @EJB
    BookRepository bookRepo;

    @EJB
    AuthorRepository authorRepo;

    private BookStockDTO newStock = new BookStockDTO();

    public void setNewStock(BookStockDTO newStock) {
        this.newStock = newStock;
    }

    private List<BookAuthorDTO> books ;

    @PostConstruct
    public void init() {
        loadAll();
        getBooksWithNoStock();
        getAuthors();
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

    public String addStock() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .post(Entity.entity(newStock, MediaType.APPLICATION_JSON));


            if (response.getStatus() == 200) {
                System.out.println("Stock added successfully.");
                loadAll();
                newStock = new BookStockDTO();
            } else {
                System.err.println("Failed to add stock: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        loadAll();
        return null;
    }

    public String deleteStock(Integer stockId) {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (stockId == null) {
                System.err.println("Stock ID is null.");
                return null;
            }
            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock/" + stockId)
                    .request()
                    .header("Authorization", "Bearer " + jwt)
                    .delete();

            BookStockDTO deletedStock = response.readEntity(BookStockDTO.class);
            if (response.getStatus() == 200) {
                System.out.println("Delete response status: " + response.getStatus() + deletedStock);
            } else {
                System.err.println("Failed to delete stock: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        loadAll();
        return "stock.xhtml?faces-redirect=true";
    }



    public String updateStock() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (newStock == null || newStock.getId() == null) {
                System.err.println("Stock Id is null.");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/stock")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .put(Entity.entity(newStock, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                System.out.println("Book updated successfully.");
                loadAll();
                newStock = new BookStockDTO();
                return "index?faces-redirect=true";
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

        return null;
    }

    public List<BookAuthorDTO> getBooksWithNoStock() {
        books = bookRepo.findBooksWithoutStock();
        return books;
    }

    public List<Author> getAuthors(){
        authors = authorRepo.getAuthors();
        return authors;
    }

    public String getBookTitleById(int id) {
        BookAuthorDTO book = bookRepo.getBookById(id);
        return book != null ? book.getTitle(): "Unknown";
    }


    public void setStockList(List<BookStockDTO> stockList) {
        this.stockList = stockList;
    }

    public void setMaxStock(List<BookStockDTO> maxStock) {
        this.maxStock = maxStock;
    }

    public void setMinStock(List<BookStockDTO> minStock) {
        this.minStock = minStock;
    }

    public void setSortedStock(List<BookStockDTO> sortedStock) {
        this.sortedStock = sortedStock;
    }

    public void setStockById(BookStockDTO stockById) {
        this.stockById = stockById;
    }

    public void setAuthorBookSum(Integer authorBookSum) {
        this.authorBookSum = authorBookSum;
    }

    public void setAvgStock(Double avgStock) {
        this.avgStock = avgStock;
    }

    public BookStockDTO getNewStock() {
        return newStock;
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

    public List<BookAuthorDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookAuthorDTO> books) {
        this.books = books;
    }

    public Integer getSelectedBookId() {
        return selectedBookId;
    }

    public void setSelectedBookId(Integer selectedBookId) {
        this.selectedBookId = selectedBookId;
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
