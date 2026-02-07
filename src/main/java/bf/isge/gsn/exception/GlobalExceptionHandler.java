package bf.isge.gsn.exception;

import bf.isge.gsn.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire global des exceptions (GlobalExceptionHandler)
 * Centralise la gestion des erreurs pour toute l'application
 * 
 * Gère :
 * - ResourceNotFoundException (404)
 * - MethodArgumentNotValidException (400 - validation)
 * - Exception générale (500)
 * 
 * Distingue les requêtes API (/api/**) → JSON
 * des requêtes Web (navigateur) → page HTML Thymeleaf
 */
@ControllerAdvice(basePackages = "bf.isge.gsn.controller")
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Vérifie si la requête est une requête API (JSON) ou une requête Web (HTML)
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        return uri.startsWith("/api/")
                || (accept != null && accept.contains("application/json") && !accept.contains("text/html"));
    }

    /**
     * Crée une vue d'erreur HTML pour les requêtes web
     */
    private ModelAndView createErrorView(HttpStatus status, String message) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", status.value() + " - " + status.getReasonPhrase());
        mav.addObject("message", message);
        mav.setStatus(status);
        return mav;
    }

    /**
     * Gère ResourceNotFoundException (404 Not Found)
     * Levée quand une ressource n'existe pas
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request,
            HttpServletRequest httpRequest) {
        
        log.warn("Ressource non trouvée: {}", ex.getMessage());

        if (!isApiRequest(httpRequest)) {
            return createErrorView(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message("Ressource non trouvée")
                .details(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère MethodArgumentNotValidException (400 Bad Request)
     * Levée quand les données de la requête ne respectent pas les contraintes de validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request,
            HttpServletRequest httpRequest) {
        
        log.warn("Erreur de validation: {}", ex.getMessage());

        if (!isApiRequest(httpRequest)) {
            return createErrorView(HttpStatus.BAD_REQUEST, "Les données fournies ne respectent pas les contraintes requises.");
        }
        
        // Récupérer les erreurs de validation par champ
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.add(new ErrorResponse.FieldError(
                    error.getField(),
                    error.getDefaultMessage(),
                    error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null"
            ))
        );
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Erreur de validation des données")
                .details("Les données fournies ne respectent pas les contraintes requises")
                .path(request.getDescription(false).replace("uri=", ""))
                .fieldErrors(fieldErrors)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les autres exceptions (500 Internal Server Error)
     * Fallback pour les erreurs non prévues
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleGlobalException(
            Exception ex,
            WebRequest request,
            HttpServletRequest httpRequest) {
        
        log.error("Erreur serveur non gérée", ex);

        if (!isApiRequest(httpRequest)) {
            return createErrorView(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur inattendue est survenue.");
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Erreur serveur interne")
                .details(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gère IllegalArgumentException (400 Bad Request)
     * Levée pour les paramètres invalides
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request,
            HttpServletRequest httpRequest) {
        
        log.warn("Argument invalide: {}", ex.getMessage());

        if (!isApiRequest(httpRequest)) {
            return createErrorView(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Argument invalide")
                .details(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
