package app.services;

public class Validator {

    public static boolean isEmailValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        //Så checker vi for @ og punktum efter @
        int atIndex = email.indexOf("@");
        if (atIndex <= 0){
            return false; //    Betyder intet @ eller @ står forrest
        }

        String domain = email.substring(atIndex + 1);
        return domain.contains(".");
    }

    public static boolean isPasswordValid(String password) {
        if (password == null || password.trim().length() < 8) {
            return false;
        }

        // Checker for mindst ét stort bogstav og ét tal
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");

        // Checker specialtegn, som IKKE er et bogstav eller tal
        boolean hasSpecialChar = password.matches(".*[^a-zA-Z0-9].*");

        return hasUppercase && hasDigit && hasSpecialChar;
    }
}
