package ui;

import java.util.List;
import java.util.Scanner;

public class GenericUI {
            private final Scanner scanner;

        public GenericUI(Scanner scanner) {
            this.scanner = scanner;
        }

        // Generisk metode: Virker til List<PromptCategory>, List<String> osv.
        public <T> T chooseFromList(String title, List<T> options) {
            System.out.println("\n" + title);
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
            System.out.println("0. Annullér");

            while (true) {
                System.out.print("Nummer: ");
                int choice = readNumber();

                if (choice == 0) {
                    return null; // Brugeren annullerede
                }
                if (choice > 0 && choice <= options.size()) {
                    return options.get(choice - 1);
                }
                System.out.println("Skriv et tal mellem 0 og " + options.size() + ".");
            }
        }

        public int readNumber() {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

