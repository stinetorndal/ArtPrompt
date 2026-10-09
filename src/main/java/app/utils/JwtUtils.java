    package app.utils;

    import com.auth0.jwt.JWT;
    import com.auth0.jwt.JWTVerifier;
    import com.auth0.jwt.algorithms.Algorithm;
    import com.auth0.jwt.interfaces.DecodedJWT;

    import java.time.Instant;
    import java.time.temporal.ChronoUnit;

    public class JwtUtils {

        private static final Algorithm ALGORITHM = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
        private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

        private JwtUtils() {}

        public static String createToken(String email, String role) {
            Instant now = Instant.now();
            return JWT.create()
                    .withSubject(email)
                    .withClaim("role", role)
                    .withIssuedAt(now)
                    .withExpiresAt(now.plus(24, ChronoUnit.HOURS))
                    .sign(ALGORITHM);
        }

        public static DecodedJWT verifyToken(String token) {
            return VERIFIER.verify(token);
        }

        public static String getEmailFromToken(String token) {
            return verifyToken(token).getSubject();
        }
    }