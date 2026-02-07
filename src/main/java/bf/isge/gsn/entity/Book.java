package bf.isge.gsn.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Entité Book représentant un livre dans la base de données
 * 
 * Attributs :
 * - id : identifiant unique (auto-généré)
 * - titre : titre du livre
 * - auteur : auteur du livre
 * - prix : prix du livre
 */

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représentation d'un livre dans le système")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique du livre", example = "1")
    private Long id;

    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(min = 1, max = 255, message = "Le titre doit contenir entre 1 et 255 caractères")
    @Column(nullable = false, length = 255, unique = true)
    @Schema(description = "Titre du livre", example = "Le Parachutage")
    private String titre;

    @NotBlank(message = "L'auteur ne peut pas être vide")
    @Size(min = 1, max = 255, message = "L'auteur doit contenir entre 1 et 255 caractères")
    @Column(nullable = false, length = 255)
    @Schema(description = "Auteur du livre", example = "Norbert ZONGO")
    private String auteur;

    @NotNull(message = "Le prix ne peut pas être null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @DecimalMax(value = "150000.00", message = "Le prix ne peut pas dépasser 150000 CFA")
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "Prix du livre en CFA", example = "10000")
    private BigDecimal prix;

}
