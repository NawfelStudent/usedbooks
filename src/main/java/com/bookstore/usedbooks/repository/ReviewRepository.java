package com.bookstore.usedbooks.repository;

import com.bookstore.usedbooks.model.Review;
import com.bookstore.usedbooks.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByReviewedUser(User reviewedUser, Pageable pageable);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedUser = :user")
    Double calculateAverageRatingForUser(User user);
}

