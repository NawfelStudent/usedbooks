package com.bookstore.usedbooks.dto;

import com.bookstore.usedbooks.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Book.BookCondition condition;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private Long sellerId;
    private String sellerName;
    private LocalDateTime listedDate;
    private boolean sold;
    private List<String> imageUrls;
    private Book.BookType type;
    private String audioUrl;
    private Integer durationMinutes;
}

