package com.bookstore.usedbooks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Identifiant unique pour chaque livre, généré automatiquement par la base de données.

    @Column(nullable = false)
    private String title;
    // Titre du livre, ne peut pas être nul.

    @Column(nullable = false)
    private String author;
    // Auteur du livre, ne peut pas être nul.

    private String isbn;
    // Numéro ISBN du livre, utilisé pour identifier de manière unique les éditions de livres.

    @Column(length = 1000)
    private String description;
    // Description du livre, avec une longueur maximale de 1000 caractères.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCondition condition;
    // Condition du livre (nouveau, comme neuf, très bon, bon, acceptable, mauvais), ne peut pas être nul.

    @Column(nullable = false)
    private BigDecimal price;
    // Prix du livre, ne peut pas être nul.

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    // Catégorie à laquelle appartient le livre, relation Many-to-One avec la classe Category.

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
    // Vendeur du livre, relation Many-to-One avec la classe User, ne peut pas être nul.

    @Column(nullable = false)
    private LocalDateTime listedDate;
    // Date à laquelle le livre a été mis en vente, ne peut pas être nul.

    private boolean sold = false;
    // Indique si le livre a été vendu ou non, par défaut à false.

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookImage> images = new HashSet<>();
    // Ensemble d'images associées au livre, relation One-to-Many avec la classe BookImage.

    @Enumerated(EnumType.STRING)
    private BookType type = BookType.PHYSICAL;
    // Type de livre (physique ou audio), par défaut à PHYSICAL.

    // Pour les livres audio
    private String audioUrl;
    // URL du fichier audio pour les livres audio.

    private Integer durationMinutes;
    // Durée du livre audio en minutes.

    public enum BookCondition {
        NEW, LIKE_NEW, VERY_GOOD, GOOD, ACCEPTABLE, POOR
    }
    // Enumération représentant les différentes conditions possibles pour un livre.

    public enum BookType {
        PHYSICAL, AUDIO
    }
    // Enumération représentant les différents types de livres (physique ou audio).
}

