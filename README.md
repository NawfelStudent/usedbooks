# Used Books - Librairie de Livres d'Occasion

Une plateforme web pour acheter et vendre des livres d'occasion. Ce projet est développé en utilisant Spring Boot pour le backend.

## Fonctionnalités

- Gestion des utilisateurs (inscription, connexion, rôles)
- Ajout, modification et suppression de livres
- Gestion des paniers d'achat
- Gestion des commandes
- Système de notation et d'avis
- Interface utilisateur avec Thymeleaf

## Prérequis

- Java 17 ou version supérieure
- Maven 3.9.9 ou version supérieure
- Base de données H2 (intégrée)

## Installation

1. Clonez le dépôt :
   ```bash
   git clone <URL_DU_DEPOT>
   cd backend_spring-boot
   ```

2. Configurez les propriétés de l'application dans `src/main/resources/application.properties` si nécessaire.

3. Compilez et exécutez le projet :
   ```bash
   ./mvnw spring-boot:run
   ```

4. Accédez à l'application à l'adresse suivante :
   ```
   http://localhost:8080
   ```

## Structure du Projet

- **src/main/java** : Contient le code source Java
  - `controller` : Contrôleurs REST
  - `service` : Services métier
  - `repository` : Interfaces pour accéder à la base de données
  - `model` : Entités JPA
  - `dto` : Objets de transfert de données
- **src/main/resources** : Contient les ressources statiques et les templates Thymeleaf
- **pom.xml** : Fichier de configuration Maven

## Contribution

Les contributions sont les bienvenues ! Veuillez soumettre une pull request ou ouvrir une issue pour discuter des modifications.

