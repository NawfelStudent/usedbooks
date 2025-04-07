package com.bookstore.usedbooks.service;

import com.bookstore.usedbooks.dto.ReviewDto;
import com.bookstore.usedbooks.exception.ResourceNotFoundException;
import com.bookstore.usedbooks.model.Order;
import com.bookstore.usedbooks.model.Review;
import com.bookstore.usedbooks.model.User;
import com.bookstore.usedbooks.repository.OrderRepository;
import com.bookstore.usedbooks.repository.ReviewRepository;
import com.bookstore.usedbooks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    public Page<ReviewDto> getReviewsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return reviewRepository.findByReviewedUser(user, pageable)
                .map(this::convertToDto);
    }
    
    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        return convertToDto(review);
    }
    
    @Transactional
    public ReviewDto createReview(Long userId, Long reviewedUserId, Long orderId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        User reviewedUser = userRepository.findById(reviewedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewed user not found with id: " + reviewedUserId));
        
        Order order = null;
        if (orderId != null) {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        }
        
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        Review review = new Review();
        review.setUser(user);
        review.setReviewedUser(reviewedUser);
        review.setOrder(order);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());
        
        Review savedReview = reviewRepository.save(review);
        
        // Update user's average rating
        Double averageRating = reviewRepository.calculateAverageRatingForUser(reviewedUser);
        reviewedUser.setAverageRating(averageRating);
        userRepository.save(reviewedUser);
        
        return convertToDto(savedReview);
    }
    
    @Transactional
    public ReviewDto updateReview(Long id, Integer rating, String comment) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        review.setRating(rating);
        review.setComment(comment);
        
        Review updatedReview = reviewRepository.save(review);
        
        // Update user's average rating
        User reviewedUser = review.getReviewedUser();
        Double averageRating = reviewRepository.calculateAverageRatingForUser(reviewedUser);
        reviewedUser.setAverageRating(averageRating);
        userRepository.save(reviewedUser);
        
        return convertToDto(updatedReview);
    }
    
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        User reviewedUser = review.getReviewedUser();
        
        reviewRepository.delete(review);
        
        // Update user's average rating
        Double averageRating = reviewRepository.calculateAverageRatingForUser(reviewedUser);
        reviewedUser.setAverageRating(averageRating);
        userRepository.save(reviewedUser);
    }
    
    private ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setUserId(review.getUser().getId());
        reviewDto.setUserName(review.getUser().getFullName());
        reviewDto.setReviewedUserId(review.getReviewedUser().getId());
        reviewDto.setReviewedUserName(review.getReviewedUser().getFullName());
        
        if (review.getOrder() != null) {
            reviewDto.setOrderId(review.getOrder().getId());
        }
        
        reviewDto.setRating(review.getRating());
        reviewDto.setComment(review.getComment());
        reviewDto.setReviewDate(review.getReviewDate());
        
        return reviewDto;
    }
}

