package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dto.BookAuthorDTO;
import org.example.dto.BookStockDTO;
import org.example.entity.Author;
import org.example.entity.Book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Stateless
public class BookRepository {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Inject
    AuthorRepository authorRepository;

    public List<BookAuthorDTO> getBooks(){
        List<BookAuthorDTO> results = entityManager
                .createQuery("select new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id)" +
                        "from Book b", BookAuthorDTO.class)
                .getResultList();
        return results;
    }

    public List<BookAuthorDTO> getBooksByAuthorId(Integer authorId) {
        return entityManager
                .createQuery("SELECT new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id) " +
                        "FROM Book b WHERE b.author.author_id = :authorId", BookAuthorDTO.class)
                .setParameter("authorId", authorId)
                .getResultList();
    }


    public BookAuthorDTO getBookById(Integer id){

        return entityManager.createQuery(
                        "select new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id) " +
                                "from Book b where b.id = :id",
                        BookAuthorDTO.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    //@Transactional
    public BookAuthorDTO saveBook(BookAuthorDTO bookAuthorDTO){

        Author author = authorRepository.getAuthorById(bookAuthorDTO.getAuthor_id());

        Book book = new Book(bookAuthorDTO.getId(), bookAuthorDTO.getTitle(), bookAuthorDTO.getIsbn(),
                bookAuthorDTO.getPrice() , author, null, null);

        author.addBook(book);

        entityManager.persist(book);
        entityManager.flush();

        bookAuthorDTO.setId(book.getId());

        return bookAuthorDTO;

    }

    //@Transactional
    public Optional<BookAuthorDTO> deleteBook(Integer id){

        Book book = entityManager.find(Book.class , id);

        if (book == null){
            return Optional.empty();
        }


        Optional<BookAuthorDTO> opt = Optional.of(new BookAuthorDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPrice(),
                book.getAuthorId()
                ));

        Author author = authorRepository.getAuthorById(book.getAuthorId());
        author.removeBook(book);

        entityManager.remove(book);


        return opt;
    }

    //@Transactional
    public BookAuthorDTO updateBook(BookAuthorDTO bookAuthorDTO){

        Author author = authorRepository.getAuthorById(bookAuthorDTO.getAuthor_id());

        Book book = new Book(bookAuthorDTO.getId(), bookAuthorDTO.getTitle(), bookAuthorDTO.getIsbn(),
                bookAuthorDTO.getPrice() , author, null, null);

        Integer id = book.getId();

        Book book1 = entityManager.find(Book.class , id);

        if (book1 == null){
            return null;
        }
        book1.setIsbn(book.getIsbn());
        book1.setPrice(book.getPrice());
        book1.setAuthor_id(book.getAuthor_id());
        book1.setTitle(book.getTitle());


        entityManager.merge(book1);


        return bookAuthorDTO;

    }

    public List<BookAuthorDTO> findBooksWithoutStock() {
        String jpql = "SELECT new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id) " +
                "FROM Book b WHERE b.id NOT IN (SELECT s.book.id FROM Stock s)";
        return entityManager.createQuery(jpql, BookAuthorDTO.class).getResultList();
    }

    public List<BookAuthorDTO> getMatchingBooks(String name){
        return entityManager
                .createQuery("SELECT a FROM Book a WHERE LOWER(a.title) LIKE :name", BookAuthorDTO.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .getResultList();
    }

    public void setBookCoverImage(Integer bookId, byte[] imageBytes, String imagePath, String contentType) throws IOException {
        Book book = entityManager.find(Book.class, bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: ID " + bookId);
        }

        if (imageBytes == null && imagePath != null) {
            imageBytes = Files.readAllBytes(Paths.get(imagePath));
        }

        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("No image data provided.");
        }

        book.setCoverImage(imageBytes);
        book.setImageContentType(contentType);
    }


    public byte[] getCoverImageByBookId(Integer id) {
        return entityManager.createQuery(
                        "SELECT b.coverImage FROM Book b WHERE b.id = :id", byte[].class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public Book findById(Integer id) {
        return entityManager.find(Book.class, id);
    }

}
