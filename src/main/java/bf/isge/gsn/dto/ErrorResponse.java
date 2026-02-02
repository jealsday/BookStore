package bf.isge.gsn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les réponses d'erreur
 * Utilisé pour structurer et standardiser les messages d'erreur de l'API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Réponse d'erreur standardisée de l'API")
public class ErrorResponse {

    @Schema(description = "Timestamp de l'erreur", example = "2026-01-21T14:30:45")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Code HTTP de l'erreur", example = "404")
    private int status;

    @Schema(description = "Message d'erreur principal", example = "Livre non trouvé")
    private String message;

    @Schema(description = "Description détaillée de l'erreur")
    private String details;

    @Schema(description = "Chemin de la requête", example = "/api/books/999")
    private String path;

    @Schema(description = "Erreurs de validation (en cas d'erreur 400)")
    private List<FieldError> fieldErrors;

    /**
     * DTO pour les erreurs de validation par champ
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Erreur de validation sur un champ")
    public static class FieldError {

        @Schema(description = "Nom du champ", example = "titre")
        private String field;

        @Schema(description = "Message d'erreur de validation", example = "Le titre ne peut pas être vide")
        private String message;

        @Schema(description = "Valeur rejetée", example = "")
        private String rejectedValue;

    }

}
