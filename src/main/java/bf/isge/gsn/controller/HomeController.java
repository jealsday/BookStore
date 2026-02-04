package bf.isge.gsn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour la page d'accueil
 * Redirige la racine vers la liste des livres
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/books";
    }
}
