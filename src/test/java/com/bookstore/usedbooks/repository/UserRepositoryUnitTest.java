package com.bookstore.usedbooks.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.bookstore.usedbooks.model.User;
/* 
 *  ce test sur jpa (java persistence api) qui  untilise le classe User de la package model qui joue le role de cree les table de BD
 *  
 * les cas possible pour le test de la classe UserRepository
 * 
 *      - 1) il faut comprendre le role de la classe UserRepository pour savoir les cas possible de test
 *           elle cherche email et verifie si existe ou pas
 * 
 *         * findByEmai l : "Optional<User>" returne un utilisateur par son email ou null 
 *           alore il y a deux cas de test
 * 
 *         * existsByEmail : "Boolean" returne true si l'email existe sinon false
 *           alore il y a deux cas de test
 * 
 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmailExiste() {
        // prepare des donnees
        String email = "user1@test.tn";

        User user = new User();
        user.setEmail(email);
        user.setPassword("P0ssword");
        user.setFullName("user1");

        userRepository.save(user);

        // execution 
        
        User foundUser = userRepository.findByEmail(email).orElse(user);// Optional<User> , en a utilise type user pour etre sure que le type de retoure est le bon

        // verification
        assertEquals(foundUser.getEmail(), email);

    }

    @Test
    void testFindByEmailNotExiste() {
        // prepare des donnees
        String email = "user2@test.tn";

        User user = new User();
        user.setEmail("user1@test.tn");
        user.setPassword("P0ssword");
        user.setFullName("user1");

        userRepository.save(user);

        // execution 
        boolean foundUser = userRepository.findByEmail(email).isEmpty();// ici pou quoi on n'a pas mais type user , car le champ email ne peut pas entre null , d'apre la classe User dans model

        // verification
        assertTrue(foundUser);

    }

    @Test
    void testExistsByEmail() {
        // prepare des donnees
        String email = "user1@test.tn";

        User user = new User();
        user.setEmail(email);
        user.setPassword("P0ssword");
        user.setFullName("user1");

        userRepository.save(user);
        
        // execution
        boolean exists = userRepository.existsByEmail(email);

        // verification
        assertTrue(exists);
    }

    @Test
    void testNotExistsByEmail() {
        // prepare des donnees
        String email = "user1@test.tn";

        User user = new User();
        user.setEmail("user@test.tn");
        user.setPassword("P0ssword");
        user.setFullName("user1");

        userRepository.save(user);
        
        // verification
        boolean exists = userRepository.existsByEmail(email);

        // execution
        assertFalse(exists);
    }


}
