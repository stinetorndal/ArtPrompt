package app.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HandlerType;
import io.javalin.http.UnauthorizedResponse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityController {

    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    public static String createToken(String email, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", role)
                .withExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .sign(ALGORITHM);
    }

    public void authenticate(Context ctx) {
        if (ctx.method() == HandlerType.OPTIONS || isPublic(ctx)) return;

        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("Mangler 'Authorization: Bearer <token>' header");
        }

        try {
            DecodedJWT jwt = VERIFIER.verify(header.substring(7));
            ctx.attribute("userEmail", jwt.getSubject());
            ctx.attribute("userRole", jwt.getClaim("role").asString());
        } catch (JWTVerificationException e) {
            throw new UnauthorizedResponse("Ugyldig eller udløbet token");
        }
    }

    public void authorize(Context ctx) {
        if (isPublic(ctx)) return;

        String userRole = ctx.attribute("userRole");
        Set<String> allowed = allowedRoles(ctx);
        if (userRole == null || !allowed.contains(userRole)) {
            throw new ForbiddenResponse("Ingen adgang. Kræver en af følgende roller: " + allowed);
        }
    }

    private static Set<String> allowedRoles(Context ctx) {
        return ctx.routeRoles().stream().map(Object::toString).collect(Collectors.toSet());
    }

    private static boolean isPublic(Context ctx) {
        Set<String> roles = allowedRoles(ctx);
        return roles.isEmpty() || roles.contains("ANYONE");
    }
}