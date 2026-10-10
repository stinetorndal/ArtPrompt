    package app.controllers;

    import app.dtos.user.UserDTO;
    import app.services.UserService;
    import app.utils.JwtUtils;
    import io.javalin.http.Context;

    import java.util.Map;

    //Håndterer http-forespørgsler og -svar
    public class UserController {

        private final UserService userService;

        public UserController(UserService userService) { //Kan ikke instantiere den, fordi det kræver parameter (dao)
            this.userService = userService;
        }

        //HANDLER 1: registrering af bruger
        public void register(Context ctx) {
            UserDTO inputDTO = ctx.bodyAsClass(UserDTO.class);
            UserDTO resultDTO = userService.createUser(inputDTO);
            respondWithToken(ctx, 201, resultDTO);
            }

        //HANDLER 2: login
        public void login(Context ctx) {
            UserDTO inputDTO = ctx.bodyAsClass(UserDTO.class);
            UserDTO resultDTO = userService.login(inputDTO);
            respondWithToken(ctx, 200, resultDTO);

        }
        //Hjælpemetode - laver token ud fra brugerens rolle
        private void respondWithToken(Context ctx, int status, UserDTO userDTO){
            String token = JwtUtils.createToken(userDTO.getEmail(), userDTO.getRole().name());

            ctx.status(status).json(Map.of(
                    "token", token,
                    "email", userDTO.getEmail()
            ));
        }
    }
