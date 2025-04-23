package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dto.BookAuthorDTO;
import org.example.entity.Author;
import org.example.entity.Book;

import java.util.List;
import java.util.Optional;

@Stateless
public class AuthorRepository {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Inject
    BookRepository BookRepo;

    public List<Author> getAuthors(){
        List<Author> results = entityManager
                .createQuery("Select a from Author a", Author.class)
                .getResultList();
        return results;
    }
    public Author getAuthorById(Integer id){

        return entityManager.find(Author.class , id);

    }

    //@Transactional
    public Author saveAuthor(Author author){

        entityManager.persist(author);
        entityManager.flush();

        return author;

    }

    //@Transactional
    public Optional<Author> deleteAuthor(Integer id){

        Author author = entityManager.find(Author.class , id);

        if (author == null){
            return Optional.empty();
        }

        entityManager.remove(author);
        return Optional.of(author);

    }

    //@Transactional
    public Author updateAuthor(Author author){

        Integer id = author.getAuthor_id();

        Author author1 = entityManager.find(Author.class , id);

        if (author1 == null){
            return null;
        }
        author1.setAge(author.getAge());
        author1.setEmail(author.getEmail());
        author1.setGender(author.getGender());
        author1.setPhone(author.getPhone());
        author1.setFull_name(author.getFull_name());


        entityManager.merge(author1);

        return author;

    }

    public Author findAuthorByBookId(Integer id){

        BookAuthorDTO bookAuthorDTO = BookRepo.getBookById(id);
        Integer author_id = bookAuthorDTO.getAuthor_id();

        return getAuthorById(author_id);
    }

}
