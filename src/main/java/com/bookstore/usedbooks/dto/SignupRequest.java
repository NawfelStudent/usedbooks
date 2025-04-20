package com.bookstore.usedbooks.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import org.springframework.security.access.method.P;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "L'email ne doit pas être vide")
    @Size(max = 50)
    @Email
    private String email;
    
    @NotBlank(message = "Le mot de passe ne doit pas être vide")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,15}$", message = "Le mot de passe doit comporter entre 8 et 15 caractères et contenir au moins une lettre, un chiffre et un caractère spécial.")
    @Size(min = 8, max = 15)
    private String password;
    
    @NotBlank(message = "Le nom complet ne doit pas être vide")
    @Size(min = 3, max = 50)
    private String fullName;
    
    private String address;
    
    private String phoneNumber;
    
    private Set<String> roles;
}

