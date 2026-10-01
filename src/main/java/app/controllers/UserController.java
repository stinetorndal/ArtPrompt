package app.controllers;

import app.dtos.unsplash.PhotographerDTO;
import app.dtos.user.UserDTO;
import app.entities.User;
import app.services.UserService;
import io.javalin.http.Context;

//Håndterer http-forespørgsler og -svar
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { //Kan ikke instantiere fordi det kræver parameter (dao) og det sker ikke!
        this.userService = userService;
    }

    //HANDLER 1: registrering af bruger
    public void register(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

        //Kalder Service
        User createdUser = userService.createUser(userDTO.getEmail(), userDTO.getPassword());
        //Send 201 created
        ctx.status(201);
        ctx.json(new UserDTO(createdUser.getEmail(), null)); //null fordi passwrod aldrig må sendes ud eller være i koden

    }

    //HANDLER 2: login
    public void login(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        User loggedInUser = userService.login(userDTO.getEmail(), userDTO.getPassword());
        //send 200 OK
        ctx.status(200);
        ctx.json(new UserDTO(loggedInUser.getEmail(), null));
    }
}
