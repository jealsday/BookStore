package bf.isge.gsn.repository;

import bf.isge.gsn.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité Book
 * Fournit les opérations CRUD et les méthodes de recherche personnalisées
 * Optimisé pour éviter les problèmes de performance avec de grandes données
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Recherche un livre par son titre (recherche exacte)
     * @param titre le titre du livre
     * @return Optional contenant le livre s'il existe
     */
    Optional<Book> findByTitre(String titre);

    /**
     * Recherche tous les livres d'un auteur donné
     * @param auteur l'auteur du livre
     * @return Liste des livres de cet auteur
     */
    List<Book> findByAuteur(String auteur);

    /**
     * Recherche les livres contenant le titre spécifié (recherche partielle, insensible à la casse)
     * @param titre partie du titre à rechercher
     * @return Liste des livres correspondant
     */
    List<Book> findByTitreContainingIgnoreCase(String titre);

    /**
     * Recherche les livres contenant le titre spécifié avec pagination
     * Optimisé pour les grandes listes de résultats
     * @param titre partie du titre à rechercher
     * @param pageable paramètres de pagination
     * @return Page de livres correspondant
     */
    Page<Book> findByTitreContainingIgnoreCase(String titre, Pageable pageable);

    /**
     * Recherche les livres contenant l'auteur spécifié (recherche partielle, insensible à la casse)
     * @param auteur partie de l'auteur à rechercher
     * @return Liste des livres correspondant
     */
    List<Book> findByAuteurContainingIgnoreCase(String auteur);

    /**
     * Recherche les livres contenant l'auteur spécifié avec pagination
     * Optimisé pour les grandes listes de résultats
     * @param auteur partie de l'auteur à rechercher
     * @param pageable paramètres de pagination
     * @return Page de livres correspondant
     */
    Page<Book> findByAuteurContainingIgnoreCase(String auteur, Pageable pageable);

    /**
     * Recherche les livres dont le prix est inférieur ou égal au montant spécifié
     * @param prix le prix maximum
     * @return Liste des livres avec un prix inférieur ou égal
     */
    List<Book> findByPrixLessThanEqual(BigDecimal prix);

    /**
     * Recherche les livres dont le prix est inférieur ou égal au montant spécifié avec pagination
     * @param prix le prix maximum
     * @param pageable paramètres de pagination
     * @return Page de livres avec un prix inférieur ou égal
     */
    Page<Book> findByPrixLessThanEqual(BigDecimal prix, Pageable pageable);

    /**
     * Recherche les livres dont le prix est supérieur ou égal au montant spécifié
     * @param prix le prix minimum
     * @return Liste des livres avec un prix supérieur ou égal
     */
    List<Book> findByPrixGreaterThanEqual(BigDecimal prix);

    /**
     * Recherche les livres dont le prix est supérieur ou égal au montant spécifié avec pagination
     * @param prix le prix minimum
     * @param pageable paramètres de pagination
     * @return Page de livres avec un prix supérieur ou égal
     */
    Page<Book> findByPrixGreaterThanEqual(BigDecimal prix, Pageable pageable);

    /**
     * Recherche personnalisée combinée : titre ET auteur (optimisée avec DISTINCT)
     * Utilise une requête JPQL optimisée avec DISTINCT pour éviter les doublons
     * @param titre partie du titre à rechercher
     * @param auteur partie de l'auteur à rechercher
     * @param pageable paramètres de pagination
     * @return Page de livres correspondant aux deux critères
     */
    @Query("SELECT DISTINCT b FROM Book b WHERE LOWER(b.titre) LIKE LOWER(CONCAT('%', :titre, '%')) " +
           "AND LOWER(b.auteur) LIKE LOWER(CONCAT('%', :auteur, '%'))")
    Page<Book> findByTitreAndAuteurContaining(@Param("titre") String titre, 
                                               @Param("auteur") String auteur, 
                                               Pageable pageable);

}
