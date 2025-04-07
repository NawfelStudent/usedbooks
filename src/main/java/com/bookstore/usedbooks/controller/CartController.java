package com.bookstore.usedbooks.controller;

import com.bookstore.usedbooks.dto.CartDto;
import com.bookstore.usedbooks.security.services.UserDetailsImpl;
import com.bookstore.usedbooks.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> getMyCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartDto cart = cartService.getCartByUser(userDetails.getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> addItemToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        
        CartDto updatedCart = cartService.addItemToCart(userDetails.getId(), bookId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/items/{bookId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long bookId,
            @RequestParam Integer quantity) {
        
        CartDto updatedCart = cartService.updateCartItem(userDetails.getId(), bookId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{bookId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDto> removeItemFromCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long bookId) {
        
        CartDto updatedCart = cartService.removeItemFromCart(userDetails.getId(), bookId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.clearCart(userDetails.getId());
        return ResponseEntity.ok().build();
    }
}

