package com.bookstore.usedbooks.service;

import com.bookstore.usedbooks.dto.CartDto;
import com.bookstore.usedbooks.dto.CartItemDto;
import com.bookstore.usedbooks.exception.ResourceNotFoundException;
import com.bookstore.usedbooks.model.Book;
import com.bookstore.usedbooks.model.Cart;
import com.bookstore.usedbooks.model.CartItem;
import com.bookstore.usedbooks.model.User;
import com.bookstore.usedbooks.repository.BookRepository;
import com.bookstore.usedbooks.repository.CartRepository;
import com.bookstore.usedbooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    
    public CartDto getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        return convertToDto(cart);
    }
    
    @Transactional
    public CartDto addItemToCart(Long userId, Long bookId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        if (book.isSold()) {
            throw new IllegalStateException("Book is already sold");
        }
        
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        // Check if item already exists in cart
        boolean itemExists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(bookId)) {
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }
        
        // If item doesn't exist, add new item
        if (!itemExists) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
            cart.getItems().add(cartItem);
        }
        
        Cart updatedCart = cartRepository.save(cart);
        return convertToDto(updatedCart);
    }
    
    @Transactional
    public CartDto updateCartItem(Long userId, Long bookId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
        
        boolean itemFound = false;
        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(bookId)) {
                if (quantity <= 0) {
                    cart.getItems().remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                itemFound = true;
                break;
            }
        }
        
        if (!itemFound) {
            throw new ResourceNotFoundException("Book not found in cart with id: " + bookId);
        }
        
        Cart updatedCart = cartRepository.save(cart);
        return convertToDto(updatedCart);
    }
    
    @Transactional
    public CartDto removeItemFromCart(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
        
        boolean itemRemoved = cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        
        if (!itemRemoved) {
            throw new ResourceNotFoundException("Book not found in cart with id: " + bookId);
        }
        
        Cart updatedCart = cartRepository.save(cart);
        return convertToDto(updatedCart);
    }
    
    @Transactional
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
        
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    public List<CartItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
        
        return cart.getItems().stream().collect(Collectors.toList());
    }
    
    private CartDto convertToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        
        // Calculate total amount and convert cart items
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<CartItemDto> cartItemDtos = cart.getItems().stream()
                .map(item -> {
                    CartItemDto itemDto = convertToDto(item);
                    totalAmount.add(itemDto.getSubtotal());
                    return itemDto;
                })
                .collect(Collectors.toList());
        
        cartDto.setItems(cartItemDtos);
        cartDto.setTotalAmount(totalAmount);
        
        return cartDto;
    }
    
    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setBookId(cartItem.getBook().getId());
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
        cartItemDto.setBookAuthor(cartItem.getBook().getAuthor());
        cartItemDto.setPrice(cartItem.getBook().getPrice());
        cartItemDto.setQuantity(cartItem.getQuantity());
        
        // Calculate subtotal
        BigDecimal subtotal = cartItem.getBook().getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
        cartItemDto.setSubtotal(subtotal);
        
        return cartItemDto;
    }
}

