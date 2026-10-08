package app.config;


import app.entities.*;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);
        configuration.addAnnotatedClass(PromptWord.class);
        configuration.addAnnotatedClass(SavedImage.class);
        configuration.addAnnotatedClass(SavedPainting.class);
        // TODO: Add more entities here...
    }
}