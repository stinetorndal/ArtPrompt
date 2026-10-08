package ui;

import app.dtos.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

//Hele denne package er lavet så jeg kan huske, hvad jeg skal i frontend
public class TextUI {

    private static final Logger logger = LoggerFactory.getLogger(TextUI.class);
    private final Scanner scanner;
    private final UserUIHandler userHandler;
    //1. Gem aktive bruger. Null = ingen er logget ind:
    private UserDTO currentUser = null;
    private boolean running = true;

    public TextUI(Scanner scanner, UserUIHandler userHandler) {
        this.scanner = scanner;
        this.userHandler = userHandler;
    }
    public void start() {
        System.out.println("=== VELKOMMEN TIL ARTPROMPT ===");
        // Step 1: Bruger eller anonym
        selectUserSession();

        // Step 2: Hovedløkken kører ud fra [1]
        while (running) {
            if (currentUser == null) {
                showAnonymousMenu();
            } else {
                showLoggedInMenu();
            }
        }
        System.out.println("Tak for denne gang! På gensyn.");
    }

    // --- STEP 1: Opstart ---
    private void selectUserSession() {
        System.out.println("1. Fortsæt som anonym");
        System.out.println("2. Log ind");
        System.out.println("3. Opret ny bruger");

        int choice = readInputNumber(3);

        switch (choice) {
            case 2 -> this.currentUser = userHandler.handlerLogin();
            case 3 -> this.currentUser = userHandler.handlerLogin();
            default -> System.out.println("Du fortsætter som anonym.");
        }
    }
    // --- Anonym Menu ---
    private void showAnonymousMenu() {
        System.out.println("\n--- HOVEDMENU [Anonym] ---");
        System.out.println("1. Find 10 billeder ud fra farvevalg");
        System.out.println("2. Få to ordprompter ud fra kategorier");
        System.out.println("3. Find berømte malerier ud fra kategori");
        System.out.println("4. Log ind / Opret konto");
        System.out.println("0. Afslut");

        int choice = readInputNumber(4);

        switch (choice) {
            case 1 -> System.out.println("Farvebilleder");
            case 2 -> System.out.println("Ordprompter");
            case 3 -> System.out.println("Berømte malerier");
            case 4 -> selectUserSession(); // Giver mulighed for at logge ind undervejs
            case 0 -> running = false;
            default -> System.out.println("Ugyldigt valg.");
        }
    }

    // --- Logget Ind Menu ---
    private void showLoggedInMenu() {
        System.out.println("\n--- HOVEDMENU [Logget ind som: " + currentUser.getEmail() + "] ---");
        System.out.println("1. Find 10 billeder ud fra farvevalg");
        System.out.println("2. Få to ordprompter ud fra kategorier");
        System.out.println("3. Find berømte malerier ud fra kategori");
        System.out.println("4. Mine Noter (opret/se)");
        System.out.println("5. Mine Gemte Billeder");
        System.out.println("6. Log ud");
        System.out.println("0. Afslut");

        int choice = readInputNumber(6);

        switch (choice) {
            case 1 -> System.out.println("Farvebilleder");
            case 2 -> System.out.println("Ordprompter");
            case 3 -> System.out.println("Berømte malerier");
            case 4 -> System.out.println("Noter");
            case 5 -> System.out.println("Gemte billeder");
            case 6 -> {
                System.out.println("Du er nu logget ud.");
                this.currentUser = null;
                selectUserSession(); // Spørger igen hvad man vil
            }
            case 0 -> running = false;
            default -> System.out.println("Ugyldigt valg.");
        }
    }

    // Hjælpermetode - fanger hvis bruger IKKE intaster 1,2,3
    private int readInputNumber(int maxChoice) {
        System.out.print("Vælg (0-" + maxChoice + "): ");
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            logger.warn("Ugyldigt input, skriv tal mellem 1-3", input);
            return -1;
        }
    }
}