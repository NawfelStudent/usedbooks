package com.bookstore.usedbooks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Identifiant unique pour chaque utilisateur, généré automatiquement par la base de données.

    @Column(nullable = false, unique = true)
    private String email;
    // Adresse e-mail de l'utilisateur, ne peut pas être nulle et doit être unique.

    @Column(nullable = false)
    private String password;
    // Mot de passe de l'utilisateur, ne peut pas être nul.

    @Column(nullable = false)
    private String fullName;
    // Nom complet de l'utilisateur, ne peut pas être nul.

    private String address;
    // Adresse de l'utilisateur.

    private String phoneNumber;
    // Numéro de téléphone de l'utilisateur.

    @Column(nullable = false)
    private boolean emailVerified = false;
    // Indique si l'adresse e-mail de l'utilisateur a été vérifiée, par défaut à false.

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    //Spécifie la table de jointure pour la relation Many-to-Many entre User et Role.
    
    private Set<Role> roles = new HashSet<>();
    // Rôles de l'utilisateur, relation Many-to-Many avec la classe Role.

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Book> booksForSale = new HashSet<>();
    // Livres mis en vente par l'utilisateur, relation One-to-Many avec la classe Book.

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();
    // Commandes passées par l'utilisateur, relation One-to-Many avec la classe Order.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Review> reviewsGiven = new HashSet<>();
    // Avis donnés par l'utilisateur, relation One-to-Many avec la classe Review.

    @OneToMany(mappedBy = "reviewedUser", cascade = CascadeType.ALL)
    private Set<Review> reviewsReceived = new HashSet<>();
    // Avis reçus par l'utilisateur, relation One-to-Many avec la classe Review.

    private Double averageRating;
    // Note moyenne de l'utilisateur basée sur les avis reçus.
}

