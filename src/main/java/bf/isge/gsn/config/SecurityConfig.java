package bf.isge.gsn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de Spring Security
 * 
 * Authentification basique avec utilisateurs en mémoire
 * Autorisations par rôle (ROLE_USER, ROLE_ADMIN)
 * 
 * Utilisateurs par défaut :
 * - user / password (ROLE_USER) - Lecture seule
 * - admin / admin123 (ROLE_ADMIN) - Lecture, Écriture, Suppression
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure les règles d'accès aux endpoints
     * 
     * GET /api/books* - Accessible à tous les utilisateurs authentifiés
     * POST /api/books - Réservé aux ADMIN
     * PUT /api/books/* - Réservé aux ADMIN
     * PATCH /api/books/* - Réservé aux ADMIN
     * DELETE /api/books/* - Réservé aux ADMIN
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Endpoints de documentation publics
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // GET - Accessible à tous les utilisateurs authentifiés
                .requestMatchers("GET", "/api/books/**").hasAnyRole("USER", "ADMIN")
                
                // POST, PUT, PATCH - Réservé aux ADMIN
                .requestMatchers("POST", "/api/books").hasRole("ADMIN")
                .requestMatchers("PUT", "/api/books/**").hasRole("ADMIN")
                .requestMatchers("PATCH", "/api/books/**").hasRole("ADMIN")
                
                // DELETE - Réservé aux ADMIN
                .requestMatchers("DELETE", "/api/books/**").hasRole("ADMIN")
                
                // Toutes les autres requêtes doivent être authentifiées
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {})
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        return http.build();
    }

    /**
     * Gestionnaire d'utilisateurs en mémoire pour développement
     * 
     * Utilisateurs disponibles :
     * - Utilisateur : user / password (ROLE_USER)
     * - Administrateur : admin / admin123 (ROLE_ADMIN)
     */

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Encodeur de mot de passe BCrypt
     */
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
