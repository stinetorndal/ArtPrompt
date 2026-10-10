package app.controllers;

import app.routes.Role;
import app.utils.JwtUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HandlerType;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

//Denne klasse har to opgaver: Authenticate + authorize (hvem er du, må du det her)
public class SecurityController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
    //Start på header. En klient sender Authorization: Bearer[token]
    private static final String BEARER_PREFIX = "Bearer ";

    // 1.CHECKER token og gemmer brugeroplysninger på context
    public void authenticate(Context ctx) {
        //OPTIONS er browsers CORS-request eller off. route = metoden skal stoppe
        if (ctx.method() == HandlerType.OPTIONS || isPublic(ctx)) {
            return;
        }
        //Her hentes header,der SKAL starte med Bearer. Ellers kastes fejl
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            logger.warn("Autentificering fejlede: Manglende eller forkert Authorization header fra IP: {} på rute: {}", ctx.ip(), ctx.path());
            throw new UnauthorizedResponse("Mangler 'Authorization: Bearer <token>' header");
        }
    //Her klippes Bearer af, så token er tilbage. Signatur og udløbstid checkes
        DecodedJWT jwt;
        Role role;
        try {
            jwt = JwtUtils.verifyToken(header.substring(BEARER_PREFIX.length()));
            //Laver  tekststreng fra token om til ENUM
            //Ved fejl får klient samme genriske besked
            role = Role.valueOf(jwt.getClaim("role").asString());
        } catch (JWTVerificationException | IllegalArgumentException | NullPointerException e) {
            logger.warn("Autentificering fejlede: Ugyldig/udløbet token fra IP: {}. Fejl: {}", ctx.ip(), e.getMessage());
            throw new UnauthorizedResponse("Ugyldig eller udløbet token");
        }

        String email = jwt.getSubject();
        ctx.attribute("userEmail", email);
        ctx.attribute("userRole", role);

        logger.debug("Bruger {} autentificeret med succes på rute: {}", email, ctx.path());
    }

    // 2. Tjekker om brugerens rolle matcher rutens påkrævede roller
    public void authorize(Context ctx) {
        if (isPublic(ctx)) {
            return;
        }

        Role userRole = ctx.attribute("userRole");
        String userEmail = ctx.attribute("userEmail");
        Set<RouteRole> allowed = ctx.routeRoles();

        if (userRole == null || !allowed.contains(userRole)) {
            logger.warn("Autorisation nægtet for bruger: {} (Rolle: {}) på rute: {}. Krævede roller: {}",
                    userEmail, userRole, ctx.path(), allowed);
            throw new ForbiddenResponse("Ingen adgang. Kræver en af følgende roller: " + allowed);
        }

        logger.debug("Bruger {} autoriseret (Rolle: {}) til rute: {}", userEmail, userRole, ctx.path());
    }

    private static boolean isPublic(Context ctx) {
        Set<RouteRole> roles = ctx.routeRoles();
        return roles.isEmpty() || roles.contains(Role.ANYONE);
    }
}