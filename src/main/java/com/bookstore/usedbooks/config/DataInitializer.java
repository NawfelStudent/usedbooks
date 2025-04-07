package com.bookstore.usedbooks.config;

import com.bookstore.usedbooks.model.Book;
import com.bookstore.usedbooks.model.Book.BookCondition;
import com.bookstore.usedbooks.model.Book.BookType;
import com.bookstore.usedbooks.model.Category;
import com.bookstore.usedbooks.model.Role;
import com.bookstore.usedbooks.model.User;
import com.bookstore.usedbooks.repository.BookRepository;
import com.bookstore.usedbooks.repository.CategoryRepository;
import com.bookstore.usedbooks.repository.RoleRepository;
import com.bookstore.usedbooks.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initializeData() {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName(Role.ERole.ROLE_USER);
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName(Role.ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }

        // Créer des catégories
        Category fiction = new Category();
        fiction.setName("Fiction");
        categoryRepository.save(fiction);

        Category nonFiction = new Category();
        nonFiction.setName("Non-Fiction");
        categoryRepository.save(nonFiction);

        // Créer des utilisateurs
        User seller1 = new User();
        seller1.setFullName("nawfel_ben_maamar");
        seller1.setPassword("password1");
        seller1.setEmail("nawfel1@example.tn");
        seller1.setPhoneNumber("+216 98123654");
        seller1.setAddress("Tunis, Tunisia");
        userRepository.save(seller1);

        User seller2 = new User();
        seller2.setFullName("nawfel_ben_maamar");
        seller2.setPassword("password");
        seller2.setEmail("nawfel@example.tn");
        seller2.setPhoneNumber("+216 44963258");
        seller2.setAddress("Tunis, Tunisia");
        userRepository.save(seller2);

        // Créer des catégories de livres
        
        Category programming = new Category();
        programming.setName("Programming");
        programming.setDescription("Books related to programming and software development.");
        categoryRepository.save(programming);

        Category testing = new Category();
        testing.setName("Testing");
        testing.setDescription("Books related to software testing and quality assurance.");
        categoryRepository.save(testing);
        

         // Créer des livres
         Book book1 = new Book();
         book1.setTitle("Effective Java");
         book1.setAuthor("Joshua Bloch");
         book1.setIsbn("9780134685991");
         book1.setDescription("A comprehensive guide to best practices in Java programming.");
         book1.setCondition(BookCondition.NEW);
         book1.setPrice(new BigDecimal("45.99"));
         book1.setCategory(programming);
         book1.setSeller(seller1);
         book1.setListedDate(LocalDateTime.now());
         book1.setType(BookType.PHYSICAL);
         bookRepository.save(book1);
 
         Book book2 = new Book();
         book2.setTitle("Spring Boot in Action");
         book2.setAuthor("Craig Walls");
         book2.setIsbn("9781617292545");
         book2.setDescription("A hands-on guide to building modern applications with Spring Boot.");
         book2.setCondition(BookCondition.LIKE_NEW);
         book2.setPrice(new BigDecimal("39.99"));
         book2.setCategory(programming);
         book2.setSeller(seller2);
         book2.setListedDate(LocalDateTime.now());
         book2.setType(BookType.PHYSICAL);
         bookRepository.save(book2);

         Book book3 = new Book();
         book3.setTitle("JUnit 5: Test Driven Development");
         book3.setAuthor("Ken Kousen");
         book3.setIsbn("9781491950357");
         book3.setDescription("A practical guide to writing tests with JUnit 5.");
         book3.setCondition(BookCondition.VERY_GOOD);
         book3.setPrice(new BigDecimal("29.99"));
         book3.setCategory(testing);
         book3.setSeller(seller2);
         book3.setListedDate(LocalDateTime.now());
         book3.setType(BookType.PHYSICAL);
         bookRepository.save(book3);
     
 
    }
}

