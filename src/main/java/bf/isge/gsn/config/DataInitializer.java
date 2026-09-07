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
        // Recreer les livres si aucun n'a de couverture (migration)
        if (bookRepository.count() > 0 && bookRepository.findAll().stream()
                .allMatch(b -> b.getCouvertureUrl() == null || b.getCouvertureUrl().isEmpty())) {
            bookRepository.deleteAll();
            log.info("Anciens livres supprimes pour migration avec couvertures");
        }

        if (bookRepository.count() == 0) {
            List<Book> demoBooks = Arrays.asList(
                // Litterature africaine - Burkina Faso
                createBook("Le Parachutage", "Norbert ZONGO", 8500,
                    "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Roman politique sur les realites du Burkina Faso, une oeuvre majeure de la litterature burkinabe.",
                    1988, "978-2-7087-0523-1"),

                createBook("Rougbeinga", "Norbert ZONGO", 7500,
                    "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Une plongee dans la societe burkinabe a travers les yeux d'un enfant des rues.",
                    1990, null),

                createBook("Le Journal intime d'une epouse", "Monique ILBOUDO", 9000,
                    "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Recit intime d'une femme africaine moderne confrontee aux traditions.",
                    2000, null),

                // Litterature africaine - Senegal
                createBook("Une si longue lettre", "Mariama BA", 7000,
                    "https://covers.openlibrary.org/b/isbn/2708702424-L.jpg",
                    "Litterature africaine",
                    "Chef-d'oeuvre de la litterature africaine feminine, ce roman epistolaire aborde la polygamie et la condition feminine au Senegal.",
                    1979, "978-2-7087-0242-1"),

                createBook("Les Bouts de bois de Dieu", "Ousmane SEMBENE", 8000,
                    "https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Recit epique de la greve des cheminots de la ligne Dakar-Niger en 1947-1948.",
                    1960, "978-2-266-02678-3"),

                createBook("Le Ventre de l'Atlantique", "Fatou DIOME", 9500,
                    "https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Entre le Senegal et la France, un regard lucide sur l'immigration et le reve europeen.",
                    2003, "978-2-253-10984-4"),

                // Litterature africaine - Cameroun
                createBook("Une vie de boy", "Ferdinand OYONO", 6000,
                    "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Journal d'un jeune domestique africain au service d'un commandant colonial. Une critique acerbe du colonialisme.",
                    1956, "978-2-266-16969-4"),

                createBook("Le Vieux Negre et la Medaille", "Ferdinand OYONO", 6500,
                    "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Satire mordante de la colonisation a travers l'histoire de Meka et sa medaille.",
                    1956, null),

                createBook("Ville cruelle", "Mongo BETI", 7500,
                    "https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Premier roman de Mongo Beti, peinture realiste de la vie urbaine africaine sous domination coloniale.",
                    1954, null),

                // Litterature africaine - Cote d'Ivoire
                createBook("Les Soleils des independances", "Ahmadou KOUROUMA", 8500,
                    "https://images.unsplash.com/photo-1535905557558-afc4877a26fc?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Roman fondateur qui revolutionna l'ecriture africaine en francais. Histoire de Fama, prince dechu.",
                    1968, "978-2-02-025918-0"),

                createBook("Allah n'est pas oblige", "Ahmadou KOUROUMA", 9000,
                    "https://covers.openlibrary.org/b/isbn/2020419785-L.jpg",
                    "Litterature africaine",
                    "Recit poignant d'un enfant-soldat africain. Prix Renaudot et Goncourt des lyceens 2000.",
                    2000, "978-2-02-041978-3"),

                // Litterature africaine - Congo
                createBook("Le Pleurer-Rire", "Henri LOPES", 7000,
                    "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=400&h=600&fit=crop",
                    "Litterature africaine",
                    "Satire politique sur les dictatures africaines post-independance.",
                    1982, null),

                // Litterature africaine - Guinee
                createBook("L'Enfant noir", "Camara LAYE", 6000,
                    "https://covers.openlibrary.org/b/isbn/2259000193-L.jpg",
                    "Litterature africaine",
                    "Recit autobiographique d'une enfance en Guinee, classique de la litterature africaine.",
                    1953, "978-2-266-16576-4"),

                // Classiques francais
                createBook("Les Miserables", "Victor HUGO", 12000,
                    "https://covers.openlibrary.org/b/isbn/2070409228-L.jpg",
                    "Classique",
                    "Chef-d'oeuvre de la litterature francaise, fresque sociale du XIXe siecle a travers le destin de Jean Valjean.",
                    1862, "978-2-07-040922-8"),

                createBook("Le Petit Prince", "Antoine de SAINT-EXUPERY", 5500,
                    "https://covers.openlibrary.org/b/isbn/2070612759-L.jpg",
                    "Classique",
                    "Conte poetique et philosophique, l'un des ouvrages les plus traduits au monde.",
                    1943, "978-2-07-061275-8"),

                createBook("L'Etranger", "Albert CAMUS", 6500,
                    "https://covers.openlibrary.org/b/isbn/2070360024-L.jpg",
                    "Classique",
                    "Roman philosophique sur l'absurdite de l'existence. Prix Nobel de litterature 1957.",
                    1942, "978-2-07-036002-4"),

                createBook("Germinal", "Emile ZOLA", 8500,
                    "https://covers.openlibrary.org/b/isbn/2070409309-L.jpg",
                    "Classique",
                    "Fresque sociale sur les mineurs du Nord de la France au XIXe siecle.",
                    1885, "978-2-07-040930-3"),

                // Litterature contemporaine
                createBook("Harry Potter a l'ecole des sorciers", "J.K. ROWLING", 11000,
                    "https://covers.openlibrary.org/b/isbn/2070541274-L.jpg",
                    "Fantasy",
                    "Premier tome de la saga Harry Potter. Le debut d'une aventure magique qui a conquis le monde.",
                    1997, "978-2-07-054127-4"),

                createBook("Le Seigneur des Anneaux", "J.R.R. TOLKIEN", 15000,
                    "https://covers.openlibrary.org/b/isbn/2267011255-L.jpg",
                    "Fantasy",
                    "Epopee fantastique incontournable, reference du genre heroic fantasy.",
                    1954, "978-2-267-01125-4"),

                createBook("1984", "George ORWELL", 7500,
                    "https://covers.openlibrary.org/b/isbn/2070368228-L.jpg",
                    "Science-fiction",
                    "Roman dystopique visionnaire sur le totalitarisme et la surveillance de masse.",
                    1949, "978-2-07-036822-8"),

                // Developpement personnel
                createBook("L'Alchimiste", "Paulo COELHO", 8000,
                    "https://covers.openlibrary.org/b/isbn/2290004448-L.jpg",
                    "Developpement personnel",
                    "Conte philosophique sur la quete de sa legende personnelle. Best-seller international.",
                    1988, "978-2-290-00444-2"),

                createBook("Pere riche, Pere pauvre", "Robert KIYOSAKI", 12500,
                    "https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=400&h=600&fit=crop",
                    "Developpement personnel",
                    "Guide sur l'education financiere et la creation de richesse.",
                    1997, "978-2-89225-556-4"),

                // Sciences et Technologie
                createBook("Une breve histoire du temps", "Stephen HAWKING", 14000,
                    "https://covers.openlibrary.org/b/isbn/2080812587-L.jpg",
                    "Technologie",
                    "Vulgarisation scientifique sur les grandes questions de l'univers par le celebre physicien.",
                    1988, "978-2-08-081258-0"),

                createBook("Clean Code", "Robert C. MARTIN", 25000,
                    "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=400&h=600&fit=crop",
                    "Technologie",
                    "Guide essentiel pour ecrire du code propre et maintenable. Reference pour tout developpeur.",
                    2008, "978-0-13-235088-4"),

                createBook("Design Patterns", "Gang of Four", 35000,
                    "https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=400&h=600&fit=crop",
                    "Technologie",
                    "Catalogue des 23 patterns de conception fondamentaux en programmation orientee objet.",
                    1994, "978-0-201-63361-0"),

                // Philosophie
                createBook("Le Monde de Sophie", "Jostein GAARDER", 9500,
                    "https://covers.openlibrary.org/b/isbn/2020219492-L.jpg",
                    "Philosophie",
                    "Introduction a l'histoire de la philosophie sous forme de roman. Ideal pour decouvrir la philosophie.",
                    1991, "978-2-02-021949-2"),

                createBook("Ainsi parlait Zarathoustra", "Friedrich NIETZSCHE", 8000,
                    "https://images.unsplash.com/photo-1506880018603-83d5b814b5a6?w=400&h=600&fit=crop",
                    "Philosophie",
                    "Oeuvre majeure de Nietzsche presentant les concepts du surhomme et de l'eternel retour.",
                    1883, "978-2-07-032294-6"),

                // Histoire
                createBook("Soundjata ou l'epopee mandingue", "Djibril Tamsir NIANE", 7000,
                    "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=400&h=600&fit=crop",
                    "Histoire",
                    "Recit de la fondation de l'Empire du Mali par Soundjata Keita au XIIIe siecle.",
                    1960, "978-2-7087-0078-6"),

                createBook("Thomas Sankara: L'espoir assassine", "Bruno JAFFRE", 11000,
                    "https://images.unsplash.com/photo-1569025690938-a00729c9e1f9?w=400&h=600&fit=crop",
                    "Biographie",
                    "Biographie complete du leader revolutionnaire burkinabe, figure emblematique de l'Afrique.",
                    2007, null)
            );

            bookRepository.saveAll(demoBooks);
            log.info("{} livres de demonstration ajoutes a la bibliotheque", demoBooks.size());
        }
    }

    private Book createBook(String titre, String auteur, int prix, String couvertureUrl,
                           String categorie, String description, Integer annee, String isbn) {
        Book book = new Book();
        book.setTitre(titre);
        book.setAuteur(auteur);
        book.setPrix(BigDecimal.valueOf(prix));
        book.setCouvertureUrl(couvertureUrl);
        book.setCategorie(categorie);
        book.setDescription(description);
        book.setAnneePublication(annee);
        book.setIsbn(isbn);
        return book;
    }
}
