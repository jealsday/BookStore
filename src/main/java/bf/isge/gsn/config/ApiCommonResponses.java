package bf.isge.gsn.config;

import bf.isge.gsn.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation personnalisée pour centraliser la documentation Swagger des réponses d'erreur
 * Réduit considérablement la duplication du code en regroupant les réponses d'erreur courantes
 * 
 * Erreurs documentées :
 * - 400 Bad Request : Données invalides
 * - 404 Not Found : Ressource non trouvée
 * - 500 Internal Server Error : Erreur serveur
 * 
 * Utilisation :
 * @GetMapping("/{id}")
 * @ApiCommonResponses
 * public ResponseEntity<Book> getBookById(@PathVariable Long id) { ... }
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "400",
        description = "Données invalides",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Ressource non trouvée",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Erreur serveur",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
})
public @interface ApiCommonResponses {
}
