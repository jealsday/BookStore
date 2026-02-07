# BookStore - Application de Gestion de Livres

## Vue d'ensemble

Application web complète développée avec **Spring Boot** pour gérer une collection de livres via une **API REST** et une **interface web Thymeleaf**, documentée avec **Swagger UI** et sécurisée avec **Spring Security**.

---

## Technologies utilisées

| Technologie | Version | Utilité |
|-------------|---------|---------|
| **Spring Boot** | 3.5.8 | Framework web |
| **Spring Data JPA** | 3.5.8 | Couche de persistance |
| **Spring Security** | 3.5.8 | Authentification / Autorisation |
| **Thymeleaf** | 3.x | Interface web (vues HTML) |
| **H2 Database** | 2.x | BD développement (fichier local) |
| **PostgreSQL** | 15+ | BD production |
| **Springdoc OpenAPI** | 2.7.0 | Documentation Swagger UI |
| **Lombok** | 1.18.x | Réduction du boilerplate |
| **Java** | 17 | Langage de programmation |

---

## Architecture

```
bf.isge.gsn
├── config/          # Configuration (Security, DataInitializer, Swagger)
├── controller/      # Contrôleurs MVC (Thymeleaf) et REST (API JSON)
├── dto/             # Objets de transfert (ErrorResponse)
├── entity/          # Entités JPA (Book, AppUser)
├── exception/       # Gestion centralisée des erreurs
├── repository/      # Repositories Spring Data JPA
└── service/         # Couche métier
```

---

## Installation et Démarrage

### Prérequis
- Java 17+
- Maven 3.8+

### Démarrage
```bash
git clone <https://github.com/jealsday/BookStore>
cd BookStore
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8080**

---

## Accès

| Ressource | URL |
|-----------|-----|
| Interface web | http://localhost:8080/books |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Console H2 | http://localhost:8080/h2-console |
| Inscription | http://localhost:8080/register |

### Authentification
- **Admin par défaut** : `admin` / `admin123` (accès complet)
- Les nouveaux utilisateurs s'inscrivent via `/register` (rôle USER - lecture seule)

### Console H2
- **JDBC URL** : `jdbc:h2:file:./data/bookstoredb`
- **Username** : `sa` | **Password** : (vide)

---

## API REST - Endpoints principaux

| Méthode | URL | Description | Rôle |
|---------|-----|-------------|------|
| GET | `/api/books` | Liste paginée des livres | USER, ADMIN |
| GET | `/api/books/{id}` | Détail d'un livre | USER, ADMIN |
| POST | `/api/books` | Créer un livre | ADMIN |
| PUT | `/api/books/{id}` | Modifier un livre | ADMIN |
| PATCH | `/api/books/{id}` | Modifier partiellement | ADMIN |
| DELETE | `/api/books/{id}` | Supprimer un livre | ADMIN |
| GET | `/api/books/search/titre?titre=...` | Recherche par titre | USER, ADMIN |
| GET | `/api/books/search/auteur?auteur=...` | Recherche par auteur | USER, ADMIN |
| GET | `/api/books/search/max-price?prix=...` | Filtrer par prix max | USER, ADMIN |
| GET | `/api/books/search/min-price?prix=...` | Filtrer par prix min | USER, ADMIN |

---

## Modèle de données

### Entité Book

| Champ | Type | Contraintes | Description |
|-------|------|-------------|-------------|
| `id` | Long | Auto-généré | Identifiant unique |
| `titre` | String | @NotBlank, max 255, unique | Titre du livre |
| `auteur` | String | @NotBlank, max 255 | Auteur du livre |
| `prix` | BigDecimal | @NotNull, 0 < prix ≤ 150000 | Prix en CFA |

---

## Gestion des erreurs

L'application distingue automatiquement les réponses :
- **Navigateur** → page HTML d'erreur
- **API REST** (`/api/**`) → réponse JSON structurée

| Code | Description |
|------|-------------|
| 400 | Données invalides / validation |
| 404 | Ressource non trouvée |
| 500 | Erreur serveur |

---

## Auteur

**ISGE** - Institut Supérieur de Génie Informatique — 2026
