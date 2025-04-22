package org.example.repository;


import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dto.BookStockDTO;
import org.example.entity.Book;
import org.example.entity.Stock;
import org.example.entity.Author;

import java.util.List;

@Stateless

public class StockRepository {

    @PersistenceContext(unitName = "myUnit")
    EntityManager entityManager;

    @Inject
    AuthorRepository AuthorRepo;

    public List<Stock> getStock(){
        List<Stock> results = entityManager
                .createQuery("Select s from Stock s", Stock.class)
                .getResultList();
        return results;
    }

    public Stock getStockById(Integer id){

        return entityManager.find(Stock.class , id);

    }

    public BookStockDTO getMaxStock(){

        return entityManager.createQuery(
                "select new org.example.dto.BookStockDTO(s.book.title, s.quantity)" +
                        " from Stock s where s.quantity = (select MAX(s2.quantity) from Stock s2)",
                        BookStockDTO.class
                )
                .getSingleResult();

    }

    public BookStockDTO getMinStock(){

        return entityManager.createQuery(
                        "select new org.example.dto.BookStockDTO(s.book.title, s.quantity)" +
                                " from Stock s where s.quantity = (select MIN(s2.quantity) from Stock s2)",
                        BookStockDTO.class
                )
                .getSingleResult();

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
                .createQuery("select new org.example.dto.BookStockDTO(s.book.title, s.quantity)" +
                        " from Stock s order by s.quantity desc", BookStockDTO.class)
                .getResultList();
        return results;

    }
}
