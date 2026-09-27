package app.config;


import app.entities.Note;
import app.entities.PromptWord;
import app.entities.SavedImage;
import app.entities.User;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Note.class);
        configuration.addAnnotatedClass(PromptWord.class);
        configuration.addAnnotatedClass(SavedImage.class);
        // TODO: Add more entities here...
    }
}