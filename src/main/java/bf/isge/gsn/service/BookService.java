package bf.isge.gsn.service;

import bf.isge.gsn.entity.Book;
import bf.isge.gsn.exception.ResourceNotFoundException;
import bf.isge.gsn.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service métier pour la gestion des livres
 * Contient la logique métier et les traitements CRUD
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Récupère tous les livres avec pagination
     * @param pageable les paramètres de pagination
     * @return Page contenant les livres
     */
    @Transactional(readOnly = true)
    public Page<Book> getAllBooks(Pageable pageable) {
        log.info("Récupération de tous les livres avec pagination: page={}, size={}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findAll(pageable);
    }

    /**
     * Récupère tous les livres sans pagination
     * @return Liste de tous les livres
     */
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        log.info("Récupération de tous les livres");
        return bookRepository.findAll();
    }

    /**
     * Récupère un livre par son identifiant
     * @param id l'identifiant du livre
     * @return le livre trouvé
     * @throws ResourceNotFoundException si le livre n'existe pas
     */
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        log.info("Récupération du livre avec l'ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Livre non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Livre non trouvé avec l'ID: " + id);
                });
    }

    /**
     * Crée un nouveau livre
     * @param book le livre à créer
     * @return le livre créé avec son ID généré
     */
    public Book saveBook(Book book) {
        log.info("Création d'un nouveau livre: titre={}, auteur={}, prix={}", 
                 book.getTitre(), book.getAuteur(), book.getPrix());
        Book savedBook = bookRepository.save(book);
        log.info("Livre créé avec succès avec l'ID: {}", savedBook.getId());
        return savedBook;
    }

    /**
     * Met à jour un livre existant
     * @param id l'identifiant du livre
     * @param bookDetails les nouvelles données du livre
     * @return le livre mis à jour
     * @throws ResourceNotFoundException si le livre n'existe pas
     */
    public Book updateBook(Long id, Book bookDetails) {
        log.info("Mise à jour du livre avec l'ID: {}", id);
        Book book = getBookById(id);
        
        book.setTitre(bookDetails.getTitre());
        book.setAuteur(bookDetails.getAuteur());
        book.setPrix(bookDetails.getPrix());
        
        Book updatedBook = bookRepository.save(book);
        log.info("Livre mis à jour avec succès: ID={}", id);
        return updatedBook;
    }

    /**
     * Met à jour partiellement un livre (patch)
     * Gère correctement les champs null : ne les met pas à jour s'ils sont null ou vides
     * @param id l'identifiant du livre
     * @param bookDetails les données partielles à mettre à jour (null = pas de changement)
     * @return le livre mis à jour
     * @throws ResourceNotFoundException si le livre n'existe pas
     */
    public Book partialUpdateBook(Long id, Book bookDetails) {
        log.info("Mise à jour partielle du livre avec l'ID: {}", id);
        Book book = getBookById(id);
        
        // Mise à jour seulement si le champ n'est pas null et n'est pas vide (pour les chaînes)
        if (bookDetails.getTitre() != null && !bookDetails.getTitre().isBlank()) {
            log.debug("Mise à jour du titre: {} → {}", book.getTitre(), bookDetails.getTitre());
            book.setTitre(bookDetails.getTitre());
        }
        if (bookDetails.getAuteur() != null && !bookDetails.getAuteur().isBlank()) {
            log.debug("Mise à jour de l'auteur: {} → {}", book.getAuteur(), bookDetails.getAuteur());
            book.setAuteur(bookDetails.getAuteur());
        }
        if (bookDetails.getPrix() != null && bookDetails.getPrix().compareTo(BigDecimal.ZERO) > 0) {
            log.debug("Mise à jour du prix: {} → {}", book.getPrix(), bookDetails.getPrix());
            book.setPrix(bookDetails.getPrix());
        }
        
        Book updatedBook = bookRepository.save(book);
        log.info("Livre mis à jour partiellement avec succès: ID={}", id);
        return updatedBook;
    }

    /**
     * Supprime un livre par son identifiant
     * @param id l'identifiant du livre à supprimer
     * @throws ResourceNotFoundException si le livre n'existe pas
     */
    public void deleteBook(Long id) {
        log.info("Suppression du livre avec l'ID: {}", id);
        Book book = getBookById(id);
        bookRepository.delete(book);
        log.info("Livre supprimé avec succès: ID={}", id);
    }

    /**
     * Recherche les livres par titre (recherche partielle)
     * @param titre partie du titre à rechercher
     * @return Liste des livres correspondant
     */
    @Transactional(readOnly = true)
    public List<Book> searchByTitre(String titre) {
        log.info("Recherche de livres par titre: {}", titre);
        return bookRepository.findByTitreContainingIgnoreCase(titre);
    }

    /**
     * Recherche les livres par titre avec pagination
     * @param titre partie du titre à rechercher
     * @param pageable paramètres de pagination
     * @return Page de livres correspondant
     */
    @Transactional(readOnly = true)
    public Page<Book> searchByTitre(String titre, Pageable pageable) {
        log.info("Recherche paginée de livres par titre: titre={}, page={}, size={}", 
                 titre, pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findByTitreContainingIgnoreCase(titre, pageable);
    }

    /**
     * Recherche les livres par auteur (recherche partielle)
     * @param auteur partie de l'auteur à rechercher
     * @return Liste des livres correspondant
     */
    @Transactional(readOnly = true)
    public List<Book> searchByAuteur(String auteur) {
        log.info("Recherche de livres par auteur: {}", auteur);
        return bookRepository.findByAuteurContainingIgnoreCase(auteur);
    }

    /**
     * Recherche les livres par auteur avec pagination
     * @param auteur partie de l'auteur à rechercher
     * @param pageable paramètres de pagination
     * @return Page de livres correspondant
     */
    @Transactional(readOnly = true)
    public Page<Book> searchByAuteur(String auteur, Pageable pageable) {
        log.info("Recherche paginée de livres par auteur: auteur={}, page={}, size={}", 
                 auteur, pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findByAuteurContainingIgnoreCase(auteur, pageable);
    }

    /**
     * Recherche les livres par titre ET auteur combinés (recherche avancée)
     * @param titre partie du titre
     * @param auteur partie de l'auteur
     * @param pageable paramètres de pagination
     * @return Page de livres correspondant aux deux critères
     */

    @Transactional(readOnly = true)
    public Page<Book> searchByTitreAndAuteur(String titre, String auteur, Pageable pageable) {
        log.info("Recherche combinée (titre ET auteur): titre={}, auteur={}, page={}, size={}", 
                 titre, auteur, pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findByTitreAndAuteurContaining(titre, auteur, pageable);
    }

    /**
     * Recherche les livres avec un prix inférieur ou égal au montant spécifié
     * @param prix le prix maximum
     * @return Liste des livres avec un prix inférieur ou égal
     */
    
    @Transactional(readOnly = true)
    public List<Book> searchByMaxPrice(BigDecimal prix) {
        log.info("Recherche de livres avec un prix <= {}", prix);
        return bookRepository.findByPrixLessThanEqual(prix);
    }

    /**
     * Recherche les livres avec un prix inférieur ou égal au montant spécifié avec pagination
     * @param prix le prix maximum
     * @param pageable paramètres de pagination
     * @return Page de livres avec un prix inférieur ou égal
     */
    @Transactional(readOnly = true)
    public Page<Book> searchByMaxPrice(BigDecimal prix, Pageable pageable) {
        log.info("Recherche paginée avec prix max: prix={}, page={}, size={}", 
                 prix, pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findByPrixLessThanEqual(prix, pageable);
    }

    /**
     * Recherche les livres avec un prix supérieur ou égal au montant spécifié
     * @param prix le prix minimum
     * @return Liste des livres avec un prix supérieur ou égal
     */
    @Transactional(readOnly = true)
    public List<Book> searchByMinPrice(BigDecimal prix) {
        log.info("Recherche de livres avec un prix >= {}", prix);
        return bookRepository.findByPrixGreaterThanEqual(prix);
    }

    /**
     * Recherche les livres avec un prix supérieur ou égal au montant spécifié avec pagination
     * @param prix le prix minimum
     * @param pageable paramètres de pagination
     * @return Page de livres avec un prix supérieur ou égal
     */
    @Transactional(readOnly = true)
    public Page<Book> searchByMinPrice(BigDecimal prix, Pageable pageable) {
        log.info("Recherche paginée avec prix min: prix={}, page={}, size={}", 
                 prix, pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findByPrixGreaterThanEqual(prix, pageable);
    }

    /**
     * Compte le nombre total de livres
     * @return le nombre de livres
     */
    @Transactional(readOnly = true)
    public long countBooks() {
        log.info("Comptage du nombre total de livres");
        return bookRepository.count();
    }

}
