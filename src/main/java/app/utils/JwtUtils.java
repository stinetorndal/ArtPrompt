    package app.utils;


    import com.auth0.jwt.JWT;
    import com.auth0.jwt.JWTVerifier;
    import com.auth0.jwt.algorithms.Algorithm;
    import com.auth0.jwt.interfaces.DecodedJWT;
    import java.time.Instant;
    import java.time.temporal.ChronoUnit;

    public class JwtUtils {

        //Næste to oprettes EN gang ved hvert kald. Static final = Oprettes EN gang, når klasse oprettes
        private static final Algorithm ALGORITHM = Algorithm.HMAC256(System.getenv("JWT_SECRET")); //Signerer med HMA+env = lås
        private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build(); //Færdigbygget verify-objekt. Checker signatur og udløbstid
        private static final long EXPIRATION_HOURS = 3; //Udløbstid i timer

        private JwtUtils (){} //privat konstruktør så ingen kan skrive  new JwtUtils

        //Denne metoder bygger token
        public static String createToken (String email, String role){
            Instant now = Instant.now(); //gemmer tidspunkt lige nu
            return JWT.create()
                    .withSubject(email) //hvem er token til
                    .withClaim("role", role) //Om hvilken rolle
                    .withIssuedAt(now) //Hvornår blev det lavet
                    .withExpiresAt(now.plus(EXPIRATION_HOURS, ChronoUnit.HOURS))
                    .sign(ALGORITHM);
        }

        //Denne metode checker signatur og udløbstid på en klients token
        public static DecodedJWT verifyToken (String token){
            return VERIFIER.verify(token);
        }

        public static String getEmailFromToken (String token){
            return verifyToken(token).getSubject();
        }
    }