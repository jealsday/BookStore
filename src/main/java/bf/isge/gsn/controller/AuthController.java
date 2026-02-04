package bf.isge.gsn.controller;

import bf.isge.gsn.entity.AppUser;
import bf.isge.gsn.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur pour l'authentification et l'inscription
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (username.length() < 3) {
            model.addAttribute("error", "Le nom d'utilisateur doit contenir au moins 3 caractères.");
            model.addAttribute("username", username);
            return "auth/register";
        }

        if (password.length() < 4) {
            model.addAttribute("error", "Le mot de passe doit contenir au moins 4 caractères.");
            model.addAttribute("username", username);
            return "auth/register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            model.addAttribute("username", username);
            return "auth/register";
        }

        if (appUserRepository.existsByUsername(username)) {
            model.addAttribute("error", "Ce nom d'utilisateur est déjà pris.");
            model.addAttribute("username", username);
            return "auth/register";
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        appUserRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Compte créé avec succès ! Connectez-vous.");
        return "redirect:/login";
    }
}
