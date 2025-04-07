package com.bookstore.usedbooks.service;

import com.bookstore.usedbooks.dto.BookDto;
import com.bookstore.usedbooks.dto.BookRequest;
import com.bookstore.usedbooks.exception.ResourceNotFoundException;
import com.bookstore.usedbooks.model.Book;
import com.bookstore.usedbooks.model.BookImage;
import com.bookstore.usedbooks.model.Category;
import com.bookstore.usedbooks.model.User;
import com.bookstore.usedbooks.repository.BookRepository;
import com.bookstore.usedbooks.repository.CategoryRepository;
import com.bookstore.usedbooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    
    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findBySoldFalse(pageable)
                .map(this::convertToDto);
    }
    
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDto(book);
    }
    
    public Page<BookDto> getBooksByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return bookRepository.findByCategory(category, pageable)
                .map(this::convertToDto);
    }
    
    public Page<BookDto> getBooksBySeller(Long sellerId, Pageable pageable) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + sellerId));
        return bookRepository.findBySeller(seller, pageable)
                .map(this::convertToDto);
    }
    
    public Page<BookDto> searchBooks(String keyword, Pageable pageable) {
        return bookRepository.searchBooks(keyword, pageable)
                .map(this::convertToDto);
    }
    
    public Page<BookDto> getBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return bookRepository.findByPriceBetweenAndSoldFalse(minPrice, maxPrice, pageable)
                .map(this::convertToDto);
    }
    
    public Page<BookDto> getBooksByCondition(Book.BookCondition condition, Pageable pageable) {
        return bookRepository.findByConditionAndSoldFalse(condition, pageable)
                .map(this::convertToDto);
    }
    
    public Page<BookDto> getBooksByType(Book.BookType type, Pageable pageable) {
        return bookRepository.findByTypeAndSoldFalse(type, pageable)
                .map(this::convertToDto);
    }
    
    @Transactional
    public BookDto createBook(BookRequest bookRequest, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + sellerId));
        
        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookRequest.getCategoryId()));
        
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setDescription(bookRequest.getDescription());
        book.setCondition(bookRequest.getCondition());
        book.setPrice(bookRequest.getPrice());
        book.setCategory(category);
        book.setSeller(seller);
        book.setListedDate(LocalDateTime.now());
        book.setSold(false);
        book.setType(bookRequest.getType());
        book.setAudioUrl(bookRequest.getAudioUrl());
        book.setDurationMinutes(bookRequest.getDurationMinutes());
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }
    
    @Transactional
    public BookDto updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + bookRequest.getCategoryId()));
        
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setDescription(bookRequest.getDescription());
        book.setCondition(bookRequest.getCondition());
        book.setPrice(bookRequest.getPrice());
        book.setCategory(category);
        book.setType(bookRequest.getType());
        book.setAudioUrl(bookRequest.getAudioUrl());
        book.setDurationMinutes(bookRequest.getDurationMinutes());
        
        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }
    
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }
    
    @Transactional
    public void markBookAsSold(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        book.setSold(true);
        bookRepository.save(book);
    }
    
    @Transactional
    public void addBookImage(Long bookId, String imageUrl) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        BookImage bookImage = new BookImage();
        bookImage.setBook(book);
        bookImage.setImageUrl(imageUrl);
        
        book.getImages().add(bookImage);
        bookRepository.save(book);
    }
    
    private BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setDescription(book.getDescription());
        bookDto.setCondition(book.getCondition());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategoryId(book.getCategory().getId());
        bookDto.setCategoryName(book.getCategory().getName());
        bookDto.setSellerId(book.getSeller().getId());
        bookDto.setSellerName(book.getSeller().getFullName());
        bookDto.setListedDate(book.getListedDate());
        bookDto.setSold(book.isSold());
        bookDto.setType(book.getType());
        bookDto.setAudioUrl(book.getAudioUrl());
        bookDto.setDurationMinutes(book.getDurationMinutes());
        
        // Convert book images to URLs
        bookDto.setImageUrls(book.getImages().stream()
                .map(BookImage::getImageUrl)
                .collect(Collectors.toList()));
        
        return bookDto;
    }
}

