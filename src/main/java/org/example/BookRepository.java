package org.example;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Stateless
public class BookRepository {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;


    public List<Book> getBooks(){
        List<Book> results = entityManager
                .createQuery("Select b from Book b", Book.class)
                .getResultList();
        return results;
    }

    public Book getBookById(Integer id){

        return entityManager.find(Book.class , id);

    }

    //@Transactional
    public Book saveBook(Book book){

        entityManager.persist(book);
        entityManager.flush();

        return book;

    }

    //@Transactional
    public Optional<Book> deleteBook(Integer id){

        Book book = entityManager.find(Book.class , id);

        if (book == null){
            return Optional.empty();
        }

        entityManager.remove(book);
        return Optional.of(book);

    }

    //@Transactional
    public Book updateBook(Book book){

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

        return book;

    }
}
