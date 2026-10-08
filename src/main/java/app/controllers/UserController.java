package app.controllers;

import app.dtos.user.UserDTO;
import app.services.UserService;
import io.javalin.http.Context;

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

        ctx.status(201).json(resultDTO);
        }

    //HANDLER 2: login
    public void login(Context ctx) {
        UserDTO inputDTO = ctx.bodyAsClass(UserDTO.class);
        UserDTO resultDTO = userService.login(inputDTO);
        //send 200 OK
        ctx.status(200).json(resultDTO);
    }
}
