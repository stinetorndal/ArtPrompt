package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@ToString

public class SavedPainting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Billed-id hos ekstern udbyder, så samme billede ikke kan gemmes to gange
    private String externalId;
    //Bruges til at vise thumbnail i frontend
    private String url;
    private String artist;
    private String title;
    private int year;

    // Mange malerier til en bruger
    @ManyToOne
    //Undgår uendelig løkke i ToString hvis student kalder course og omvendt
    @ToString.Exclude
    private User user;

    public SavedPainting(Long id, String externalId, String url, String artist, String title, int year, User user) {
        this.id = id;
        this.externalId = externalId;
        this.url = url;
        this.artist = artist;
        this.title = title;
        this.year = year;
        this.user = user;
    }
}
