package com.bookstore.usedbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long reviewedUserId;
    private String reviewedUserName;
    private Long orderId;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
}

