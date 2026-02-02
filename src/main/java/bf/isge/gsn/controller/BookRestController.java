package bf.isge.gsn.controller;

import bf.isge.gsn.config.ApiCommonResponses;
import bf.isge.gsn.entity.Book;
import bf.isge.gsn.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des livres
 * API REST exposant les endpoints CRUD
 * 
 * Endpoints disponibles :
 * - GET /api/books - Récupérer tous les livres avec pagination
 * - GET /api/books/{id} - Récupérer un livre par ID
 * - POST /api/books - Créer un nouveau livre
 * - PUT /api/books/{id} - Modifier complètement un livre
 * - PATCH /api/books/{id} - Modifier partiellement un livre
 * - DELETE /api/books/{id} - Supprimer un livre
 * - GET /api/books/search/titre - Rechercher par titre
 * - GET /api/books/search/auteur - Rechercher par auteur
 */

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Books", description = "API pour la gestion des livres")
public class BookRestController {

    private final BookService bookService;

    /**
     * Récupère tous les livres avec pagination
     * @param pageable paramètres de pagination (page, size, sort)
     * @return Page de livres
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les livres", description = "Récupère la liste paginée de tous les livres")
    @ApiResponse(responseCode = "200", description = "Liste des livres récupérée avec succès")
    public ResponseEntity<Page<Book>> getAllBooks(
            @Parameter(description = "Paramètres de pagination (page, size, sort)")
            Pageable pageable) {
        log.info("GET /api/books - Récupération de tous les livres");
        Page<Book> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Récupère tous les livres sans pagination
     * @return Liste de tous les livres
     */
    @GetMapping("/all")
    @Operation(summary = "Récupérer tous les livres (sans pagination)", description = "Récupère la liste complète de tous les livres")
    @ApiResponse(responseCode = "200", description = "Liste des livres récupérée avec succès")
    public ResponseEntity<List<Book>> getAllBooksWithoutPagination() {
        log.info("GET /api/books/all - Récupération de tous les livres sans pagination");
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Récupère un livre par son identifiant
     * @param id l'identifiant du livre
     * @return le livre recherché
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livre par ID", description = "Récupère les détails d'un livre spécifique")
    @ApiCommonResponses
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "Identifiant unique du livre", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/books/{} - Récupération du livre", id);
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Crée un nouveau livre
     * @param book les données du nouveau livre
     * @return le livre créé avec son ID généré (statut 201)
     */
    @PostMapping
    @Operation(summary = "Créer un nouveau livre", description = "Crée un nouveau livre dans la base de données")
    @ApiCommonResponses
    public ResponseEntity<Book> createBook(
            @Valid @RequestBody Book book) {
        log.info("POST /api/books - Création d'un nouveau livre: {}", book);
        Book createdBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Met à jour complètement un livre (PUT)
     * @param id l'identifiant du livre à modifier
     * @param bookDetails les nouvelles données du livre
     * @return le livre mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour complètement un livre", description = "Remplace entièrement les données d'un livre")
    @ApiCommonResponses
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "Identifiant unique du livre", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Book bookDetails) {
        log.info("PUT /api/books/{} - Mise à jour du livre", id);
        Book updatedBook = bookService.updateBook(id, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Met à jour partiellement un livre (PATCH)
     * @param id l'identifiant du livre à modifier
     * @param bookDetails les données partielles à mettre à jour
     * @return le livre mis à jour
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Mettre à jour partiellement un livre", description = "Met à jour certains champs d'un livre")
    @ApiCommonResponses
    public ResponseEntity<Book> partialUpdateBook(
            @Parameter(description = "Identifiant unique du livre", example = "1")
            @PathVariable Long id,
            @RequestBody Book bookDetails) {
        log.info("PATCH /api/books/{} - Mise à jour partielle du livre", id);
        Book updatedBook = bookService.partialUpdateBook(id, bookDetails);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Supprime un livre
     * @param id l'identifiant du livre à supprimer
     * @return réponse sans contenu (204)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livre", description = "Supprime un livre de la base de données")
    @ApiResponse(responseCode = "204", description = "Livre supprimé avec succès")
    @ApiCommonResponses
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Identifiant unique du livre", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/books/{} - Suppression du livre", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche les livres par titre
     * @param titre le titre ou partie du titre à rechercher
     * @param pageable paramètres de pagination optionnels
     * @return liste des livres correspondant
     */

    @GetMapping("/search/titre")
    @Operation(summary = "Rechercher par titre", description = "Recherche les livres contenant le titre spécifié (avec pagination)")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    public ResponseEntity<?> searchByTitre(
            @Parameter(description = "Titre ou partie du titre", example = "Hugo")
            @RequestParam String titre,
            @Parameter(description = "Paramètres de pagination optionnels (page, size, sort)")
            Pageable pageable) {
        log.info("GET /api/books/search/titre - Recherche par titre: {}", titre);
        // Si pageable contient des paramètres, utiliser la version paginée
        if (pageable.isPaged()) {
            Page<Book> books = bookService.searchByTitre(titre, pageable);
            return ResponseEntity.ok(books);
        } else {
            List<Book> books = bookService.searchByTitre(titre);
            return ResponseEntity.ok(books);
        }
    }

    /**
     * Recherche les livres par auteur
     * @param auteur l'auteur ou partie de l'auteur à rechercher
     * @param pageable paramètres de pagination optionnels
     * @return liste des livres correspondant
     */

    @GetMapping("/search/auteur")
    @Operation(summary = "Rechercher par auteur", description = "Recherche les livres contenant l'auteur spécifié (avec pagination)")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    public ResponseEntity<?> searchByAuteur(
            @Parameter(description = "Auteur ou partie de l'auteur", example = "Hugo")
            @RequestParam String auteur,
            @Parameter(description = "Paramètres de pagination optionnels (page, size, sort)")
            Pageable pageable) {
        log.info("GET /api/books/search/auteur - Recherche par auteur: {}", auteur);
        if (pageable.isPaged()) {
            Page<Book> books = bookService.searchByAuteur(auteur, pageable);
            return ResponseEntity.ok(books);
        } else {
            List<Book> books = bookService.searchByAuteur(auteur);
            return ResponseEntity.ok(books);
        }
    }

    /**
     * Recherche avancée combinée : titre ET auteur
     * @param titre partie du titre à rechercher
     * @param auteur partie de l'auteur à rechercher
     * @param pageable paramètres de pagination
     * @return page de livres correspondant aux deux critères
     */
    
    @GetMapping("/search/titre-auteur")
    @Operation(summary = "Recherche avancée (titre ET auteur)", description = "Recherche les livres correspondant à la fois au titre ET à l'auteur spécifiés")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche combinée")
    public ResponseEntity<Page<Book>> searchByTitreAndAuteur(
            @Parameter(description = "Titre ou partie du titre", example = "Parachutage")
            @RequestParam String titre,
            @Parameter(description = "Auteur ou partie de l'auteur", example = "Zongo")
            @RequestParam String auteur,
            @Parameter(description = "Paramètres de pagination (page, size, sort)")
            Pageable pageable) {
        log.info("GET /api/books/search/titre-auteur - Recherche combinée: titre={}, auteur={}", titre, auteur);
        Page<Book> books = bookService.searchByTitreAndAuteur(titre, auteur, pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Recherche les livres avec un prix maximum
     * @param prix le prix maximum
     * @param pageable paramètres de pagination optionnels
     * @return liste des livres avec un prix inférieur ou égal
     */
    @GetMapping("/search/max-price")
    @Operation(summary = "Rechercher par prix maximum", description = "Récupère les livres avec un prix inférieur ou égal (avec pagination)")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    public ResponseEntity<?> searchByMaxPrice(
            @Parameter(description = "Prix maximum", example = "10000")
            @RequestParam BigDecimal prix,
            @Parameter(description = "Paramètres de pagination optionnels (page, size, sort)")
            Pageable pageable) {
        log.info("GET /api/books/search/max-price - Recherche par prix max: {}", prix);
        if (pageable.isPaged()) {
            Page<Book> books = bookService.searchByMaxPrice(prix, pageable);
            return ResponseEntity.ok(books);
        } else {
            List<Book> books = bookService.searchByMaxPrice(prix);
            return ResponseEntity.ok(books);
        }
    }

    /**
     * Recherche les livres avec un prix minimum
     * @param prix le prix minimum
     * @param pageable paramètres de pagination optionnels
     * @return liste des livres avec un prix supérieur ou égal
     */
    @GetMapping("/search/min-price")
    @Operation(summary = "Rechercher par prix minimum", description = "Récupère les livres avec un prix supérieur ou égal (avec pagination)")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    public ResponseEntity<?> searchByMinPrice(
            @Parameter(description = "Prix minimum", example = "5000")
            @RequestParam BigDecimal prix,
            @Parameter(description = "Paramètres de pagination optionnels (page, size, sort)")
            Pageable pageable) {
        log.info("GET /api/books/search/min-price - Recherche par prix min: {}", prix);
        if (pageable.isPaged()) {
            Page<Book> books = bookService.searchByMinPrice(prix, pageable);
            return ResponseEntity.ok(books);
        } else {
            List<Book> books = bookService.searchByMinPrice(prix);
            return ResponseEntity.ok(books);
        }
    }

    /**
     * Compte le nombre total de livres
     * @return nombre de livres dans la base
     */
    @GetMapping("/count")
    @Operation(summary = "Compter les livres", description = "Retourne le nombre total de livres")
    @ApiResponse(responseCode = "200", description = "Nombre de livres retourné")
    public ResponseEntity<Long> countBooks() {
        log.info("GET /api/books/count - Comptage des livres");
        long count = bookService.countBooks();
        return ResponseEntity.ok(count);
    }

}
