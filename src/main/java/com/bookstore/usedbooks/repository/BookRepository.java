package com.bookstore.usedbooks.repository;

import com.bookstore.usedbooks.model.Book;
import com.bookstore.usedbooks.model.Category;
import com.bookstore.usedbooks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findBySoldFalse(Pageable pageable);
    Page<Book> findBySeller(User seller, Pageable pageable);
    Page<Book> findByCategory(Category category, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCaseAndSoldFalse(String title, Pageable pageable);
    Page<Book> findByAuthorContainingIgnoreCaseAndSoldFalse(String author, Pageable pageable);
    Page<Book> findByPriceBetweenAndSoldFalse(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Book> findByConditionAndSoldFalse(Book.BookCondition condition, Pageable pageable);
    Page<Book> findByTypeAndSoldFalse(Book.BookType type, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE b.sold = false AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Book> searchBooks(String keyword, Pageable pageable);
}

