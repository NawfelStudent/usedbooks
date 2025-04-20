package com.bookstore.usedbooks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;



/*
 *
 *  JPA (Java Persistence API) pour cree les table dans la BD automatiquement (sont crees par le framework)
 * 
 *  Classe représentant un utilisateur dans le système.
 * Elle contient des informations sur l'utilisateur, y compris son {adresse e-mail,mot de passe, nom complet, adresse, numéro de téléphone, rôles et livres en vente}.
 * Elle est mappée à la table "users" dans la base de données.
 * 
 * Annotations :
 * - @Entity : Indique que cette classe est une entité JPA (Java Persistence API) .
 * - @Table : Spécifie le nom de la table dans la base de données.
 * - @Data : Génère automatiquement les méthodes getter, setter, equals, hashCode et toString.
 * - @NoArgsConstructor : Génère un constructeur sans arguments.
 * - @AllArgsConstructor : Génère un constructeur avec tous les arguments.
 * 
 * Attributs :
 * - id : Identifiant unique de l'utilisateur, généré automatiquement par la base de données.
 * - emailVerified : Indique si l'adresse e-mail de l'utilisateur a été vérifiée, par défaut à false.
 * - booksForSale : Livres mis en vente par l'utilisateur, relation One-to-Many avec la classe Book.
 * - orders : Commandes passées par l'utilisateur, relation One-to-Many avec la classe Order.
 * - reviewsGiven : Avis donnés par l'utilisateur, relation One-to-Many avec la classe Review.
 * - reviewsReceived : Avis reçus par l'utilisateur, relation One-to-Many avec la classe Review.
 * - averageRating : Note moyenne de l'utilisateur basée sur les avis reçus.
 * 
    * Relations :
 * - @ManyToMany : Indique une relation Many-to-Many entre User et Role.
 * - @OneToMany : Indique une relation One-to-Many entre User et Book, Order et Review.
 * - @JoinTable : Spécifie la table de jointure pour la relation Many-to-Many entre User et Role.
 * - @JoinColumn : Spécifie la colonne de jointure pour la relation One-to-Many entre User et Book, Order et Review.
 * 
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// Identifiant unique pour chaque utilisateur, généré automatiquement par la base de données.

    @Column(nullable = false, unique = true)
    private String email;// Adresse e-mail de l'utilisateur, ne peut pas être nulle et doit être unique.
    
    @Column(nullable = false)
    private String password;// Mot de passe de l'utilisateur, ne peut pas être nul.

    @Column(nullable = false)
    private String fullName;// Nom complet de l'utilisateur, ne peut pas être nul.

    private String address;// Adresse de l'utilisateur.

    private String phoneNumber;// Numéro de téléphone de l'utilisateur.

    @Column(nullable = false)
    private boolean emailVerified = false;
    // Indique si l'adresse e-mail de l'utilisateur a été vérifiée, par défaut à false.

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )//Spécifie la table de jointure pour la relation Many-to-Many entre User et Role.
    
    private Set<Role> roles = new HashSet<>();// Rôles de l'utilisateur, relation Many-to-Many avec la classe Role.

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Book> booksForSale = new HashSet<>();// Livres mis en vente par l'utilisateur, relation One-to-Many avec la classe Book.

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();// Commandes passées par l'utilisateur, relation One-to-Many avec la classe Order.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Review> reviewsGiven = new HashSet<>();// Avis donnés par l'utilisateur, relation One-to-Many avec la classe Review.

    @OneToMany(mappedBy = "reviewedUser", cascade = CascadeType.ALL)
    private Set<Review> reviewsReceived = new HashSet<>();// Avis reçus par l'utilisateur, relation One-to-Many avec la classe Review.

    private Double averageRating;// Note moyenne de l'utilisateur basée sur les avis reçus.

    public void calculateAverageRating() {
        if (this.reviewsReceived == null || this.reviewsReceived.isEmpty()) {
            this.averageRating = 0.0;
            return;
        }

        double sum = this.reviewsReceived.stream()
                .mapToDouble(Review::getRating)
                .sum();
        
        this.averageRating = sum / this.reviewsReceived.size();
    }

    public void addReview(Review review) {
        this.reviewsReceived.add(review);
        calculateAverageRating();
    }
}

