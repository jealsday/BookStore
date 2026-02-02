#  BookStore - Application de Gestion de Livres

## Vue d'ensemble

**BookStore** est une application web complète développée avec **Spring Boot** permettant de gérer une collection de livres via une **API REST** et un **interface web Thymeleaf**.

L'application expose une **API REST** documentée avec **Swagger UI** et inclut une authentification basique avec **Spring Security**.

---

##  Fonctionnalités principales

### Gestion des livres (CRUD)
- ✅ Créer un nouveau livre
- ✅ Lire/Récupérer un livre ou la liste complète
- ✅ Mettre à jour un livre (complètement ou partiellement)
- ✅ Supprimer un livre

### Recherche et Filtrage
- ✅ Recherche par titre (recherche partielle)
- ✅ Recherche par auteur (recherche partielle)
- ✅ Filtrer par prix minimum/maximum
- ✅ Pagination et tri des résultats

### Documentation et Test
- ✅ Documentation automatique via **Swagger UI** (OpenAPI 3.0)
- ✅ Test interactif des endpoints directement dans le navigateur

### Sécurité
- ✅ Authentification HTTP Basic
- ✅ Autorisation par rôles (USER, ADMIN)
- ✅ Endpoints protégés selon les opérations


##  Technologies utilisées

| Technologie | Version | Utilité |
|-----------|---------|---------|
| **Spring Boot** | 3.5.8 | Framework web |
| **Spring Data JPA** | 3.5.8 | Couche de persistance |
| **Spring Security** | 3.5.8 | Authentification/Autorisation |
| **H2 Database** | 2.x | BD développement (en mémoire) |
| **PostgreSQL** | 15+ | BD production |
| **Springdoc OpenAPI** | 2.5.0 | Documentation Swagger UI |
| **Lombok** | 1.18.x | Réduction du boilerplate |
| **Java** | 25 | Langage de programmation |

---

##  Modèle de données

### Entité Book

| Champ | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | @Id @GeneratedValue | Identifiant unique (auto-généré) |
| `titre` | String | @NotBlank, @Size(1-255) | Titre du livre |
| `auteur` | String | @NotBlank, @Size(1-255) | Auteur du livre |
| `prix` | Double | @NotNull, @DecimalMin(0), @DecimalMax(150000.00) | Prix en CFA |

---

##  Installation et Démarrage

### Prérequis
- Java 17+
- Maven 3.8+
- Git

### Étapes

1. **Cloner le repository**
```bash
git clone <url-repo>
cd BookStore
```

2. **Compiler le projet**
```bash
mvn clean install
```

3. **Démarrer l'application**
```bash
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8080**

---

##  API REST Endpoints

### Documentation interactive
- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **OpenAPI Spec** : http://localhost:8080/v3/api-docs

### Authentification
- **Utilisateur** : `user` / `password` (Lecture seule)
- **Administrateur** : `admin` / `admin123` (Lecture + Écriture + Suppression)

### Endpoints

####  Récupérer les livres

**GET** `/api/books` - Tous les livres (paginé)
```bash
curl -u user:password "http://localhost:8080/api/books?page=0&size=10"
```
Response: `200 OK` - Page de livres

**GET** `/api/books/{id}` - Un livre par ID
```bash
curl -u user:password "http://localhost:8080/api/books/1"
```
Response: `200 OK` | `404 Not Found`

**GET** `/api/books/all` - Tous les livres (sans pagination)
```bash
curl -u user:password "http://localhost:8080/api/books/all"
```
Response: `200 OK` - Liste complète

---

####  Créer un livre

**POST** `/api/books` - Créer un nouveau livre
```bash
curl -X POST -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Le Hobbit",
    "auteur": "J.R.R. Tolkien",
    "prix": 18.99
  }' \
  http://localhost:8080/api/books
```
Response: `201 Created` - Livre créé avec ID | `400 Bad Request` - Données invalides

---

####  Modifier un livre

**PUT** `/api/books/{id}` - Modifier complètement
```bash
curl -X PUT -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Le Hobbit (Nouvelle édition)",
    "auteur": "J.R.R. Tolkien",
    "prix": 19.99
  }' \
  http://localhost:8080/api/books/1
```
Response: `200 OK` | `404 Not Found`

**PATCH** `/api/books/{id}` - Modifier partiellement
```bash
curl -X PATCH -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"prix": 17.99}' \
  http://localhost:8080/api/books/1
```
Response: `200 OK` | `404 Not Found`

---

####  Supprimer un livre

**DELETE** `/api/books/{id}` - Supprimer un livre
```bash
curl -X DELETE -u admin:admin123 \
  http://localhost:8080/api/books/1
```
Response: `204 No Content` | `404 Not Found`

---

####  Recherche

**GET** `/api/books/search/titre?titre=Hugo`
```bash
curl -u user:password "http://localhost:8080/api/books/search/titre?titre=Hugo"
```

**GET** `/api/books/search/auteur?auteur=Tolkien`
```bash
curl -u user:password "http://localhost:8080/api/books/search/auteur?auteur=Tolkien"
```

**GET** `/api/books/search/max-price?prix=20`
```bash
curl -u user:password "http://localhost:8080/api/books/search/max-price?prix=20"
```

**GET** `/api/books/search/min-price?prix=10`
```bash
curl -u user:password "http://localhost:8080/api/books/search/min-price?prix=10"
```

---

####  Utilitaires

**GET** `/api/books/count` - Nombre total de livres
```bash
curl -u user:password "http://localhost:8080/api/books/count"
```
Response: `200 OK` - Nombre entier

---

##  Codes HTTP et Erreurs

### Codes de succès
| Code | Statut | Description |
|------|--------|-------------|
| `200` | OK | Requête réussie, ressource retournée |
| `201` | Created | Ressource créée avec succès |
| `204` | No Content | Ressource supprimée avec succès |

### Codes d'erreur
| Code | Statut | Description |
|------|--------|-------------|
| `400` | Bad Request | Données invalides ou paramètres manquants |
| `401` | Unauthorized | Authentification requise |
| `403` | Forbidden | Accès refusé (droits insuffisants) |
| `404` | Not Found | Ressource non trouvée |
| `500` | Internal Server Error | Erreur serveur |

---

##  Exemple de réponse d'erreur

**404 - Livre non trouvé**
```json
{
  "timestamp": "2026-01-21T14:30:45",
  "status": 404,
  "message": "Ressource non trouvée",
  "details": "Livre non trouvé avec l'ID: 999",
  "path": "/api/books/999",
  "fieldErrors": null
}
```

**400 - Erreur de validation**
```json
{
  "timestamp": "2026-01-21T14:30:45",
  "status": 400,
  "message": "Erreur de validation des données",
  "details": "Les données fournies ne respectent pas les contraintes requises",
  "path": "/api/books",
  "fieldErrors": [
    {
      "field": "titre",
      "message": "Le titre ne peut pas être vide",
      "rejectedValue": ""
    }
  ]
}
```

---

##  Configuration

### Développement (H2)
Fichier : `application.properties`
```properties
spring.datasource.url=jdbc:h2:mem:bookstoredb
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

### Production (PostgreSQL)
Fichier : `application-prod.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookstore
spring.datasource.username=postgres
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=validate
```

### Démarrer en mode production
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

##  Base de données H2

### Console H2 (développement)
Accédez à : **http://localhost:8080/h2-console**

- **JDBC URL** : `jdbc:h2:mem:bookstoredb`
- **Username** : `sa`
- **Password** : (vide)

---

##  Données d'initialisation

Le fichier `data.sql` contient **15 livres d'auteurs africains** chargés automatiquement au démarrage :

1. Le Parachutage - Norbert Zongo (Burkina Faso) - 10000 CFA
2. L'Enfant noir - Camara Laye (Guinée) - 8500 CFA
3. Une vie de boy - Ferdinand Oyono (Cameroun) - 7500 CFA
4. Choses s'effondrent - Chinua Achebe (Nigeria) - 11000 CFA
5. Peau noire, masques blancs - Frantz Fanon (Algérie) - 13000 CFA
6. King Kong et moi - Mongo Beti (Cameroun) - 9000 CFA
7. Ainsi parla l'Oncle - Jean Price-Mars (Haïti) - 12000 CFA
8. Le Chinois du Cantal - Leila Sebbar (Algérie) - 8500 CFA
9. La Grève des bàttu - Aminata Sow Fall (Sénégal) - 9500 CFA
10. Une enfance africaine - Ishmael Beah (Sierra Leone) - 10500 CFA
11. Mandela - Nelson Mandela (Afrique du Sud) - 14000 CFA
12. Amkoullel l'enfant peul - Amadou Hampaté Ba (Mali) - 11500 CFA
13. Miroir de l'Afrique - Wole Soyinka (Nigeria) - 12500 CFA
14. Bound Feet - Heba Hashem (Égypte) - 10000 CFA
15. Nos ancêtres - Amos Tutuola (Nigeria) - 9500 CFA

---

##  Modifications et Améliorations Réalisées (Février 2026)

**1. Optimisation de la recherche combinée**
   - Nouvel endpoint `/api/books/search/titre-auteur`
   - Recherche simultanée par titre ET auteur
   - Support de la pagination et du tri

**2. Amélioration de la mise à jour partielle (PATCH)**
   - Validation stricte des champs null
   - Vérification que le prix > 0
   - Prévention des mises à jour accidentelles

**3. Pagination complète des recherches**
   - Toutes les méthodes de recherche supportent la pagination
   - Retour de `Page<Book>` au lieu de `List<Book>`
   - Paramètres: `page`, `size`, `sort`

**4. Optimisation des requêtes JPQL**
   - Ajout du mot-clé `DISTINCT` dans les requêtes combinées
   - Élimination des doublons
   - Amélioration des performances

**5. Centralisation de la documentation Swagger**
   - Création de l'annotation `@ApiCommonResponses`
   - Réduction de la duplication (70+ lignes économisées)
   - Standardisation des erreurs (400, 404, 500)

**6. Mise à jour complète de la base de données**
   - Passage de 10 à 15 livres d'auteurs africains
   - Adoption de la monnaie CFA (10000-15000 CFA)
   - Représentation de 12 pays africains et de la diaspora

---

##  Validation des données

Les contraintes suivantes sont appliquées :

- **titre** : Non vide, entre 1 et 255 caractères
- **auteur** : Non vide, entre 1 et 255 caractères
- **prix** : Positif, entre 0,01 et 150000 CFA

Les violations génèrent une réponse `400 Bad Request` avec les détails des erreurs.

---

##  Outils utiles

### Tester l'API avec cURL
```bash
# Récupérer tous les livres
curl -u user:password "http://localhost:8080/api/books"

# Créer un livre
curl -X POST -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"titre":"Test","auteur":"Auteur","prix":9.99}' \
  http://localhost:8080/api/books

# Modifier un livre
curl -X PUT -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"titre":"Test Modifié","auteur":"Auteur","prix":10.99}' \
  http://localhost:8080/api/books/1

# Supprimer un livre
curl -X DELETE -u admin:admin123 http://localhost:8080/api/books/1
```

### Tester avec Postman
1. Importer la collection Swagger : http://localhost:8080/v3/api-docs
2. Ajouter l'authentification HTTP Basic (user/password ou admin/admin123)
3. Tester les endpoints

### Swagger UI
Interface graphique interactive disponible à : **http://localhost:8080/swagger-ui.html**

---

##  Logs

Les logs sont configurés avec les niveaux suivants :

- **INFO** : Opérations principales
- **DEBUG** : Détails des requêtes/réponses Spring Web
- **TRACE** : Paramètres des requêtes JPA

Fichier de configuration : `application.properties`

---

##  Contribution

1. Fork le repository
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

##  Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

---

##  Auteur

**Développement** : ISGE (Institut Supérieur de Génie Informatique)
**Année** : 2026

---

##  Support

Pour toute question ou problème :
1. Consultez la documentation Swagger UI
2. Vérifiez les fichiers de log
3. Contactez l'équipe de développement

---

**Dernière mise à jour** : 02 Février 2026
