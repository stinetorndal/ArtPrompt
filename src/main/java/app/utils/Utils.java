package app.utils;

import app.exceptions.ApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String getPropertyValue(String propName, String resourceName) {
        // 1. Tjek om variablen findes som miljøvariabel (GitHub Secrets / Deployed server)
        String envValue = System.getenv(propName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        // Tjek også uppercase/underscore format (f.eks. db.user -> DB_USER)
        String formattedKey = propName.toUpperCase().replace('.', '_');
        String envFormattedValue = System.getenv(formattedKey);
        if (envFormattedValue != null && !envFormattedValue.isBlank()) {
            return envFormattedValue.trim();
        }

        // 2. Læs fra .properties-filen hvis miljøvariabel ikke er sat
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new ApiException(500, String.format("Property file %s not found in classpath.", resourceName));
            }

            Properties prop = new Properties();
            prop.load(is);

            String value = prop.getProperty(propName);
            if (value != null) {
                return value.trim();  // Trim whitespace
            } else {
                throw new ApiException(500, String.format("Property %s not found in %s", propName, resourceName));
            }
        } catch (IOException ex) {
            throw new ApiException(500, String.format("Could not read property %s.", propName));
        }
    }
}