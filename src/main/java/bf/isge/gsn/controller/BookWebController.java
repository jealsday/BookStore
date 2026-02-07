package bf.isge.gsn.controller;

import bf.isge.gsn.entity.Book;
import bf.isge.gsn.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Contrôleur MVC pour l'interface web Thymeleaf
 * Gère les vues HTML pour la gestion des livres
 */
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookWebController {

    private final BookService bookService;

    /**
     * Affiche la liste de tous les livres
     */
    @GetMapping
    public String listBooks(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Book> books;
        if (keyword != null && !keyword.isBlank()) {
            books = bookService.searchByTitre(keyword);
            List<Book> byAuteur = bookService.searchByAuteur(keyword);
            for (Book b : byAuteur) {
                if (!books.contains(b)) {
                    books.add(b);
                }
            }
            model.addAttribute("keyword", keyword);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("totalBooks", bookService.countBooks());
        return "books/list";
    }

    /**
     * Affiche la page de recherche avancée (ADMIN uniquement)
     */
    @GetMapping("/search-advanced")
    public String showAdvancedSearch() {
        return "books/search-advanced";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau livre
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("isEdit", false);
        return "books/form";
    }

    /**
     * Traite la soumission du formulaire d'ajout
     */
    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", book.getId() != null);
            return "books/form";
        }
        if (book.getId() != null) {
            bookService.updateBook(book.getId(), book);
            redirectAttributes.addFlashAttribute("successMessage", "Le livre a été modifié avec succès.");
        } else {
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("successMessage", "Le livre a été ajouté avec succès.");
        }
        return "redirect:/books";
    }

    /**
     * Affiche le formulaire de modification d'un livre
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("isEdit", true);
        return "books/form";
    }

    /**
     * Supprime un livre et redirige vers la liste
     */
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Le livre a été supprimé avec succès.");
        return "redirect:/books";
    }

    /**
     * Affiche les détails d'un livre
     */
    @GetMapping("/{id}")
    public String showBookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "books/details";
    }

    /**
     * Redirige la racine vers la liste des livres
     */
    @GetMapping("/")
    public String redirectToBooks() {
        return "redirect:/books";
    }
}