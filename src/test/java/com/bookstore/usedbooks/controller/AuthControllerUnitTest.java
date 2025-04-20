package com.bookstore.usedbooks.controller;

import com.bookstore.usedbooks.dto.JwtResponse;
import com.bookstore.usedbooks.dto.LoginRequest;
import com.bookstore.usedbooks.dto.SignupRequest;
import com.bookstore.usedbooks.model.Role;
import com.bookstore.usedbooks.model.User;
import com.bookstore.usedbooks.repository.RoleRepository;
import com.bookstore.usedbooks.repository.UserRepository;
import com.bookstore.usedbooks.security.jwt.JwtUtils;
import com.bookstore.usedbooks.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/*
 * Unit Test Class authController
 * 
 * Q1 : c'est quoi Mockito ? 
 * Mockito est une bibliothèque Java qui permet de créer des objets simulés (mocks) pour les tests unitaires.
 * Ces objets simulés peuvent être utilisés pour simuler le comportement de dépendances externes,
 * telles que des bases de données ou des services externes, afin de tester le code de manière isolée.
 * 
 * Q2 : mock vs patch
 * pour quoi on utilise mockito ?
 * Le concept exact de patch (comme en Python) 
 * n’existe pas vraiment de la même façon en Java "car java est une longage statique" . 
 * Mais l’équivalent, c’est l’idée de "remplacer un bean par un mock"
 * 
 * le plan de test :
 * 1. prepare des objets simulés (mocks).
 * 2. Injecter ces mocks dans le contrôleur à tester.
 * 3. Préparer des données de test pour les requêtes de connexion et d'inscription.
 * 4. Configurer le comportement des mocks pour simuler les réponses attendues.
 * 5. Appeler les méthodes du contrôleur avec les données de test,
 *  
 */

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;



    private UserDetailsImpl userDetails;

    @BeforeEach
    public void setup() {
        userDetails = new UserDetailsImpl(
                1L, "user@test.tn", "P@ssw0rd", "Test User",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

//                      signin 

    @Test
    void testAuthenticateUser_Success() {
        
        LoginRequest loginRequest = new LoginRequest("user@test.tn", "P@ssw0rd");
    
        // preparation
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mocked-jwt");

        /*
         * when() any() : 
         * 
         * 
         */


        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("mocked-jwt", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("user@test.tn", jwtResponse.getEmail());
        assertEquals("Test User", jwtResponse.getFullName());
        assertTrue(jwtResponse.getRoles().contains("ROLE_USER"));
        
    }

    @Test
    void testAuthenticateUser_ErreurEmail() {
        LoginRequest loginRequest = new LoginRequest("user@erreur.tn", "P@ssw0rd");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("erreur email"));

        assertThrows(BadCredentialsException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
    }

    @Test
    void testAuthenticateUser_ErreurPassword() {
        LoginRequest loginRequest = new LoginRequest("user@test.tn", "P@ssw0rdErreur");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("erreur password"));

        assertThrows(BadCredentialsException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
    }
    
//                      signup

    @Test
    void testRegisterUser_Success_DefaultUserRole() {
        // Préparation des données spécifiques au test
        SignupRequest signUpRequest = new SignupRequest(
                "user@test.tn",
                "P@ssw0rd",
                "User Test",
                "Test Address",
                "1234567890",
                null // Aucun rôle spécifié, donc le rôle par défaut sera utilisé
        );

        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("user@test.tn");
        newUser.setPassword("P@ssw0rd");
        newUser.setFullName("User Test");
        newUser.setAddress("Test Address");
        newUser.setPhoneNumber("1234567890");

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName(Role.ERole.ROLE_USER);

        // Préparation des mocks
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false); // L'email n'existe pas encore
        when(roleRepository.findByName(Role.ERole.ROLE_USER)).thenReturn(Optional.of(userRole)); // Rôle par défaut trouvé
        when(userRepository.save(any(User.class))).thenReturn(newUser); // Simule l'enregistrement de l'utilisateur

        // Exécution
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Vérification
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());

        // Vérifie que les méthodes des mocks ont été appelées correctement
        verify(userRepository).existsByEmail(signUpRequest.getEmail());
        verify(roleRepository).findByName(Role.ERole.ROLE_USER);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_success_AdminRole() {
        SignupRequest signupRequest = new SignupRequest("admin@test.tn", 
                                                        "P@ssw0rd", 
                                                        "Admin Test", 
                                                        "Admin Address", 
                                                        "999888777", 
                                                        Set.of("admin"));

        Role adminRole = new Role(2L,Role.ERole.ROLE_ADMIN);
        

        when(userRepository.existsByEmail("admin@test.tn")).thenReturn(false);
        when(roleRepository.findByName(Role.ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());
    
    }

    @Test
    void testRegisterUser_EmailExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("exist@email.tn");

        when(userRepository.existsByEmail("exist@email.tn")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error: Email est deja existe!", response.getBody());
    }

    @Test
    void testRegisterUser_EmailNull(){
        SignupRequest signupRequest = new SignupRequest(
                "",
                "P@ssw0rd",
                "Test User",
                "Test Address",
                "1234567890",
                null
        );

        
         // Simulez une exception pour les validations
            assertThrows(RuntimeException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }

    

    @Test
    void testRegisterUser_PasswordNull() {
        SignupRequest signupRequest = new SignupRequest(
                "user@test.tn",
                "",
                "Test User",
                "Test Address",
                "1234567890",
                null
        );

        assertThrows(RuntimeException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }


    @Test
    void testRegisterUser_FullNameNull() {
        SignupRequest signupRequest = new SignupRequest(
                "user@test.tn",
                "P@ssw0rd",
                "",
                "Test Address",
                "1234567890",
                Set.of("user")
        );

        
        assertThrows(RuntimeException.class, () -> {
            authController.registerUser(signupRequest);
        });
    }
    

}