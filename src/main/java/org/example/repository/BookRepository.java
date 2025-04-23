package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dto.BookAuthorDTO;
import org.example.dto.BookStockDTO;
import org.example.entity.Book;

import java.util.List;
import java.util.Optional;

@Stateless
public class BookRepository {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;


    public List<BookAuthorDTO> getBooks(){
        List<BookAuthorDTO> results = entityManager
                .createQuery("select new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id)" +
                        "from Book b", BookAuthorDTO.class)
                .getResultList();
        return results;
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
    public BookAuthorDTO saveBook(Book book){

        entityManager.persist(book);
        entityManager.flush();

        Integer id = book.getId();

        return entityManager.createQuery(
                        "select new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id) " +
                                "from Book b where b.id = :id",
                        BookAuthorDTO.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    //@Transactional
    public Optional<BookAuthorDTO> deleteBook(Integer id){

        Book book = entityManager.find(Book.class , id);

        if (book == null){
            return Optional.empty();
        }

        Optional<BookAuthorDTO> opt = Optional.of(entityManager.createQuery(
                        "select new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id) " +
                                "from Book b where b.id = :id",
                        BookAuthorDTO.class)
                .setParameter("id", id)
                .getSingleResult());

        entityManager.remove(book);


        return opt;
    }

    //@Transactional
    public BookAuthorDTO updateBook(Book book){

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

        return entityManager.createQuery(
                        "select new org.example.dto.BookAuthorDTO(b.id, b.title, b.isbn, b.price, b.author.author_id) " +
                                "from Book b where b.id = :id",
                        BookAuthorDTO.class)
                .setParameter("id", id)
                .getSingleResult();
    }


}
