package com.bookstore.usedbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private boolean emailVerified;
    private Set<String> roles;
    private Double averageRating;
}

