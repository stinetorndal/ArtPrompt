package app.utils;

import app.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtil {
    private static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    private RequestUtil() {
    }

    // Generisk enum-parser til query-parameters (fx ?artist=REMBRANDT eller ?color=RED)
    public static <E extends Enum<E>> E getEnumQueryParam(Context ctx, String paramName, Class<E> enumClass) {
        String value = ctx.queryParam(paramName);
        if (value == null || value.trim().isEmpty()) {
            logger.warn("Forespørgsel afvist: Manglende '{}'-queryparameter", paramName);
            throw new ApiException(400, "Angiv venligst " + paramName + " som parameter");
        }
        try {
            return Enum.valueOf(enumClass, value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Forespørgsel afvist: Ugyldig værdi '{}' for '{}'", value, paramName);
            throw new ApiException(400, "Ugyldig værdi for '" + paramName + "'. Check at værdien findes");
        }
    }

    // Henter path-paramenter fra URL og konverterer til Long fx /user/{userId})
    public static Long getLongPathParam(Context ctx, String paramName) {
        String value = ctx.pathParam(paramName);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.warn("Forespørgsel afvist: Ugyldigt tal-ID '{}' for path-parameter '{}'", value, paramName);
            throw new ApiException(400, "Parameteren '" + paramName + "' skal være et gyldigt tal");
        }
    }

    //Metode til at håndtere den faktiske IP fordi alle beøgende deler bucket. Det bliver meget hurtigt 20 kald
    public static String getClientIp(Context ctx) {
        String forwarded = ctx.header("X-Forwarded-For");
        if (forwarded == null || forwarded.isBlank()) {
            return ctx.ip();
        }
        //Headeren kan "holde" en liste fx "client, proxy". Første er klient
        return forwarded.split(",")[0].trim();
    }
}

