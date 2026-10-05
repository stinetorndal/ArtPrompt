package ui;

import app.dtos.user.UserDTO;
import app.services.PromptService;
import app.services.UserService;

import java.util.Scanner;

public class TextUI {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final PromptService promptService;
    private final TextUIHandler handler;
    //1. Gem aktive bruger. Null = ingen er logget ind
    private final UserDTO currentUser = null;

    public TextUI(UserService userService, PromptService promptService, TextUIHandler handler) {
        this.userService = userService;
        this.promptService = promptService;
        this.handler = handler;
    }

    //showMainMenu

    //handlePrompts

    //handleLogin
}
