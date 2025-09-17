package org.example.pageBeans;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.client.Entity;
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
import org.example.entity.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;


import java.util.Collections;
import java.util.List;

@Named
@RequestScoped
public class BookBean {

    private List<BookAuthorDTO> books;
    private BookAuthorDTO bookById;
    private Integer searchBookId;
    private Integer selectedAuthorId;


    @EJB
    AuthorRepository authorRepository;

    @EJB
    BookRepository bookRepository;

    private BookAuthorDTO newBook = new BookAuthorDTO();

    public void setNewBook(BookAuthorDTO newBook) {
        this.newBook = newBook;
    }

    private List<Author> authors;

    private Part uploadedFile;

    @PostConstruct
    public void init() {

        loadAllBooks();
        authors = authorRepository.getAuthors();
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

    public String addBook() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (!authorExists(newBook.getAuthor_id())){
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Author ID does not exist", null));
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .post(Entity.entity(newBook, MediaType.APPLICATION_JSON));


            BookAuthorDTO createdBook = response.readEntity(BookAuthorDTO.class);
            Book book = bookRepository.findById(createdBook.getId());
            if (uploadedFile != null && uploadedFile.getSize() > 0) {
                byte[] imageBytes = uploadedFile.getInputStream().readAllBytes();
                bookRepository.setBookCoverImage(createdBook.getId(), imageBytes, null, uploadedFile.getContentType());
            } else {
                book.setCoverImage(null);
                book.setImageContentType(null);
            }

            if (response.getStatus() == 200) {
                System.out.println("Book added successfully.");
                loadAllBooks();
                newBook = new BookAuthorDTO();
                uploadedFile = null;
            } else {
                System.err.println("Failed to add book: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        loadAllBooks();
        return null;
    }


    public String deleteBook(Integer bookId) {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (bookId == null) {
                System.err.println("Book ID is null.");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book/" + bookId)
                    .request()
                    .header("Authorization", "Bearer " + jwt)
                    .delete();

            if (response.getStatus() == 200) {
                System.out.println("Book deleted successfully.");
            } else {
                System.err.println("Failed to delete book: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

        loadAllBooks();
        return "books.xhtml?faces-redirect=true";
    }


    public String updateBook() {
        Client client = ClientBuilder.newClient();
        try {
            String jwt = getJwtFromCookie();
            if (jwt == null || jwt.isBlank()) {
                System.err.println("JWT token not found.");
                return null;
            }

            if (newBook == null || newBook.getId() == null) {
                System.err.println("Book Id is null.");
                return null;
            }

            Response response = client
                    .target("http://localhost:8080/Helloworld-1.0-SNAPSHOT/api/book")
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwt)
                    .put(Entity.entity(newBook, MediaType.APPLICATION_JSON));

            if (response.getStatus() == 200) {
                System.out.println("Book updated successfully.");
                loadAllBooks();
                newBook = new BookAuthorDTO();
                return "index?faces-redirect=true";
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

        return null;
    }

    public String getAuthorNameById(int authorId) {
        Author a = authorRepository.getAuthorById(authorId);
        return a != null ? a.getFull_name() : "Unknown";
    }

    public Boolean authorExists(int id){
        Author author = authorRepository.getAuthorById(id);
        return author != null;
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

    public void setBooks(List<BookAuthorDTO> books) {
        this.books = books;
    }

    public void setBookById(BookAuthorDTO bookById) {
        this.bookById = bookById;
    }

    public BookAuthorDTO getNewBook() {
        return newBook;
    }

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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Integer getSelectedAuthorId() {
        return selectedAuthorId;
    }

    public void setSelectedAuthorId(Integer selectedAuthorId) {
        this.selectedAuthorId = selectedAuthorId;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}
