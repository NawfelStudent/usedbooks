package com.bookstore.usedbooks.dto;

import com.bookstore.usedbooks.model.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotBlank
    private String title;
    
    @NotBlank
    private String author;
    
    private String isbn;
    
    private String description;
    
    @NotNull
    private Book.BookCondition condition;
    
    @NotNull
    @Positive
    private BigDecimal price;
    
    @NotNull
    private Long categoryId;
    
    private Book.BookType type = Book.BookType.PHYSICAL;
    
    private String audioUrl;
    
    private Integer durationMinutes;
}

