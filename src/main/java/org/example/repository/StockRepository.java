package org.example.repository;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.dto.BookAuthorDTO;
import org.example.dto.BookStockDTO;
import org.example.entity.Book;
import org.example.entity.Stock;
import org.example.entity.Author;

import java.util.List;
import java.util.Optional;

@Stateless

public class StockRepository {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Inject
    BookRepository bookRepository;

    @Inject
    AuthorRepository authorRepository;


    public List<BookStockDTO> getStock(){
        List<BookStockDTO> results = entityManager
                .createQuery("select new org.example.dto.BookStockDTO(s.book.id , s.quantity)" +
                        "from Stock s", BookStockDTO.class)
                .getResultList();
        return results;
    }

    public BookStockDTO saveStock(BookStockDTO bookStockDTO){

        BookAuthorDTO bookAuthorDTO = bookRepository.getBookById(bookStockDTO.getId());
        Author author = authorRepository.getAuthorById(bookAuthorDTO.getAuthor_id());

        Book book = new Book(bookAuthorDTO.getId() , bookAuthorDTO.getTitle() , bookAuthorDTO.getIsbn() , bookAuthorDTO.getPrice() , author  );

        Stock stock= new Stock(book , bookStockDTO.getQuantity());

        entityManager.persist(stock);
        entityManager.flush();


        return bookStockDTO;

    }

    public BookStockDTO updateStock(BookStockDTO bookStockDTO){

        BookAuthorDTO bookAuthorDTO = bookRepository.getBookById(bookStockDTO.getId());
        Author author = authorRepository.getAuthorById(bookAuthorDTO.getAuthor_id());
        Book book = new Book(bookAuthorDTO.getId(), bookAuthorDTO.getTitle(), bookAuthorDTO.getIsbn(),
                bookAuthorDTO.getPrice() , author);
        Stock stock = new Stock(book , bookStockDTO.getQuantity());
        Integer id = stock.getBookId();
        Stock stock1 = entityManager.find(Stock.class , id);
        if (stock1 == null){
            return null;
        }
        stock1.setQuantity(stock.getQuantity());
        stock1.setBook(book);

        return bookStockDTO;

    }

    //@Transactional
    public Optional<BookStockDTO> deleteStock(Integer id){

        Stock stock = entityManager.find(Stock.class , id);

        if (stock == null){
            return Optional.empty();
        }


        Optional<BookStockDTO> opt = Optional.of(new BookStockDTO(
                stock.getBookId(),
                stock.getQuantity()
        ));
        Book book = stock.getBook();
        if (book != null) {
            book.setStock(null);
        }
        entityManager.remove(stock);
        entityManager.flush();
        entityManager.clear();


        return opt;
    }

    public BookStockDTO getStockById(Integer id){

        TypedQuery<BookStockDTO> query = entityManager.createQuery(
                "SELECT new org.example.dto.BookStockDTO(s.book.id, s.quantity) FROM Stock s WHERE s.book.id = :id",
                BookStockDTO.class
        );
        query.setParameter("id", id);

        List<BookStockDTO> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);

    }

    public List<BookStockDTO> getMaxStock(){

        List<BookStockDTO> results = entityManager
                .createQuery("select new org.example.dto.BookStockDTO(s.book.id, s.quantity)" +
                                " from Stock s where s.quantity = (select MAX(s2.quantity) from Stock s2)",
                        BookStockDTO.class)
                .getResultList();
        return results;
    }

    public List<BookStockDTO> getMinStock(){

        List<BookStockDTO> results = entityManager
                .createQuery("select new org.example.dto.BookStockDTO(s.book.id, s.quantity)" +
                                " from Stock s where s.quantity = (select MIN(s2.quantity) from Stock s2)",
                        BookStockDTO.class)
                .getResultList();
        return results;

    }

    //kai gia to min to idio

    public Double getAverageQuantity() {
        return entityManager.createQuery(
                        "select AVG(s.quantity) from Stock s",
                        Double.class
                )
                .getSingleResult();
    }


    //kata meso oro posa antigrafa enos vivlio exw sto stock



    //pairnw ena author_id posa antigrafa uparxoyn apo ola ta vivlia tou author

    public List<Integer> findBookIdsByAuthorId(Integer authorId) {
        return entityManager.createQuery(
                        "select b.id from Book b where b.author.id = :authorId",
                        Integer.class)
                .setParameter("authorId", authorId)
                .getResultList();
    }



    public Integer getSumOfAuthorBooks(Integer id){

        List<Integer> bookIds = findBookIdsByAuthorId(id);

        return entityManager.createQuery(
                        "select SUM(s.quantity) from Stock s where s.book.id in :bookIds",
                        Long.class)
                .setParameter("bookIds", bookIds)
                .getSingleResult()
                .intValue();    }

    //lista me titlous vivliwn kai quantity tajinomimena me vash to quantity

    public List<BookStockDTO> getSortedList(){

        List<BookStockDTO> results = entityManager
                .createQuery("select new org.example.dto.BookStockDTO(s.book.id, s.quantity)" +
                        " from Stock s order by s.quantity desc", BookStockDTO.class)
                .getResultList();
        return results;

    }
}
