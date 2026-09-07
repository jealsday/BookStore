package bf.isge.gsn.config;

import bf.isge.gsn.entity.AppUser;
import bf.isge.gsn.entity.Book;
import bf.isge.gsn.repository.AppUserRepository;
import bf.isge.gsn.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Initialise les donnees de demonstration au demarrage
 * - Compte administrateur par defaut
 * - Collection de livres varies pour demontrer les fonctionnalites
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeAdmin();
        initializeDemoBooks();
    }

    private void initializeAdmin() {
        if (!appUserRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            appUserRepository.save(admin);
            log.info("Compte administrateur cree : admin / admin123");
        }
    }

    private void initializeDemoBooks() {
        if (bookRepository.count() == 0) {
            List<Book> demoBooks = Arrays.asList(
                // Litterature africaine - Burkina Faso
                createBook("Le Parachutage", "Norbert ZONGO", 8500),
                createBook("Rougbeinga", "Norbert ZONGO", 7500),
                createBook("Le Journal intime d'une epouse", "Monique ILBOUDO", 9000),
                createBook("Moi, Taximan", "Boubacar Boris DIOP", 6500),

                // Litterature africaine - Senegal
                createBook("Une si longue lettre", "Mariama BA", 7000),
                createBook("Les Bouts de bois de Dieu", "Ousmane SEMBENE", 8000),
                createBook("Le Ventre de l'Atlantique", "Fatou DIOME", 9500),

                // Litterature africaine - Cameroun
                createBook("Une vie de boy", "Ferdinand OYONO", 6000),
                createBook("Le Vieux Negre et la Medaille", "Ferdinand OYONO", 6500),
                createBook("Ville cruelle", "Mongo BETI", 7500),

                // Litterature africaine - Cote d'Ivoire
                createBook("Les Soleils des independances", "Ahmadou KOUROUMA", 8500),
                createBook("Allah n'est pas oblige", "Ahmadou KOUROUMA", 9000),

                // Litterature africaine - Congo
                createBook("Le Pleurer-Rire", "Henri LOPES", 7000),

                // Litterature africaine - Guinee
                createBook("L'Enfant noir", "Camara LAYE", 6000),

                // Classiques francais
                createBook("Les Miserables", "Victor HUGO", 12000),
                createBook("Le Petit Prince", "Antoine de SAINT-EXUPERY", 5500),
                createBook("L'Etranger", "Albert CAMUS", 6500),
                createBook("Germinal", "Emile ZOLA", 8500),

                // Litterature contemporaine
                createBook("Harry Potter a l'ecole des sorciers", "J.K. ROWLING", 11000),
                createBook("Le Seigneur des Anneaux", "J.R.R. TOLKIEN", 15000),
                createBook("1984", "George ORWELL", 7500),

                // Developpement personnel
                createBook("L'Alchimiste", "Paulo COELHO", 8000),
                createBook("Pere riche, Pere pauvre", "Robert KIYOSAKI", 12500),

                // Sciences et Technologie
                createBook("Une breve histoire du temps", "Stephen HAWKING", 14000),
                createBook("Clean Code", "Robert C. MARTIN", 25000),
                createBook("Design Patterns", "Gang of Four", 35000),

                // Philosophie
                createBook("Le Monde de Sophie", "Jostein GAARDER", 9500),
                createBook("Ainsi parlait Zarathoustra", "Friedrich NIETZSCHE", 8000),

                // Histoire
                createBook("Soundjata ou l'epopee mandingue", "Djibril Tamsir NIANE", 7000),
                createBook("Thomas Sankara: L'espoir assassine", "Bruno JAFFRE", 11000)
            );

            bookRepository.saveAll(demoBooks);
            log.info("{} livres de demonstration ajoutes a la bibliotheque", demoBooks.size());
        }
    }

    private Book createBook(String titre, String auteur, int prix) {
        Book book = new Book();
        book.setTitre(titre);
        book.setAuteur(auteur);
        book.setPrix(BigDecimal.valueOf(prix));
        return book;
    }
}
