package com.bookstore.usedbooks.service;

import com.bookstore.usedbooks.dto.OrderDto;
import com.bookstore.usedbooks.dto.OrderItemDto;
import com.bookstore.usedbooks.exception.ResourceNotFoundException;
import com.bookstore.usedbooks.model.*;
//import com.bookstore.usedbooks.repository.BookRepository;
import com.bookstore.usedbooks.repository.OrderRepository;
import com.bookstore.usedbooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    //private final BookRepository bookRepository;
    private final BookService bookService;
    
    public Page<OrderDto> getOrdersByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return orderRepository.findByBuyer(user, pageable)
                .map(this::convertToDto);
    }
    
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDto(order);
    }
    
    public OrderDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
        return convertToDto(order);
    }
    
    @Transactional
    public OrderDto createOrder(Long userId, List<CartItem> cartItems, String shippingAddress, String paymentMethod, String transactionId) {
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setBuyer(buyer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setTransactionId(transactionId);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();
            
            if (book.isSold()) {
                throw new IllegalStateException("Book is already sold: " + book.getTitle());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setPrice(book.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            
            order.getItems().add(orderItem);
            
            // Calculate total amount
            totalAmount = totalAmount.add(book.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            
            // Mark book as sold
            bookService.markBookAsSold(book.getId());
        }
        
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }
    
    @Transactional
    public OrderDto updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        order.setStatus(status);
        
        Order updatedOrder = orderRepository.save(order);
        return convertToDto(updatedOrder);
    }
    
    private String generateOrderNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setBuyerId(order.getBuyer().getId());
        orderDto.setBuyerName(order.getBuyer().getFullName());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setStatus(order.getStatus());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setShippingAddress(order.getShippingAddress());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        
        // Convert order items
        List<OrderItemDto> orderItemDtos = order.getItems().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        orderDto.setItems(orderItemDtos);
        
        return orderDto;
    }
    
    private OrderItemDto convertToDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setBookId(orderItem.getBook().getId());
        orderItemDto.setBookTitle(orderItem.getBook().getTitle());
        orderItemDto.setBookAuthor(orderItem.getBook().getAuthor());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setQuantity(orderItem.getQuantity());
        return orderItemDto;
    }
}

