package bf.isge.gsn.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Entite Book representant un livre dans la base de donnees
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representation d'un livre dans le systeme")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique du livre", example = "1")
    private Long id;

    @NotBlank(message = "Le titre ne peut pas etre vide")
    @Size(min = 1, max = 255, message = "Le titre doit contenir entre 1 et 255 caracteres")
    @Column(nullable = false, length = 255, unique = true)
    @Schema(description = "Titre du livre", example = "Le Parachutage")
    private String titre;

    @NotBlank(message = "L'auteur ne peut pas etre vide")
    @Size(min = 1, max = 255, message = "L'auteur doit contenir entre 1 et 255 caracteres")
    @Column(nullable = false, length = 255)
    @Schema(description = "Auteur du livre", example = "Norbert ZONGO")
    private String auteur;

    @NotNull(message = "Le prix ne peut pas etre null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit etre superieur a 0")
    @DecimalMax(value = "150000.00", message = "Le prix ne peut pas depasser 150000 CFA")
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Prix du livre en CFA", example = "10000")
    private BigDecimal prix;

    @Size(max = 500, message = "L'URL de couverture ne peut pas depasser 500 caracteres")
    @Column(length = 500)
    @Schema(description = "URL de l'image de couverture", example = "https://example.com/cover.jpg")
    private String couvertureUrl;

    @Size(max = 2000, message = "La description ne peut pas depasser 2000 caracteres")
    @Column(length = 2000)
    @Schema(description = "Description ou resume du livre")
    private String description;

    @Size(max = 50, message = "La categorie ne peut pas depasser 50 caracteres")
    @Column(length = 50)
    @Schema(description = "Categorie du livre", example = "Roman")
    private String categorie;

    @Size(max = 20, message = "L'ISBN ne peut pas depasser 20 caracteres")
    @Column(length = 20)
    @Schema(description = "Numero ISBN du livre", example = "978-2-07-036822-8")
    private String isbn;

    @Min(value = 1, message = "L'annee doit etre superieure a 0")
    @Max(value = 2100, message = "L'annee ne peut pas depasser 2100")
    @Schema(description = "Annee de publication", example = "1988")
    private Integer anneePublication;
}
