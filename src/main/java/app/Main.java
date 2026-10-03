package app;

import app.config.ApplicationConfig;
import app.runner.AppRunner;

public class Main {
    public static void main(String[] args) {
        AppRunner appRunner = new AppRunner();
        appRunner.run();

        ApplicationConfig.startServer(7070);
    }
}
