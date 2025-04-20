package com.bookstore.usedbooks.config;

import com.bookstore.usedbooks.model.*;
import com.bookstore.usedbooks.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Initialiser les rôles s'ils n'existent pas
        initRoles();
        

    }
    
    private void initRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName(Role.ERole.ROLE_USER);
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName(Role.ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
            
            System.out.println("Rôles initialisés");
        }
    }
}

