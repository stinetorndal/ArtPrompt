package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class SavedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Bruges til at vise thumbnail i frontend
    private String url;
    //Billed-id hos ekstern udbyder, så samme billede ikke kan gemmes to gange
    private String externalId;
    private String title;
    //Skal bruges til at vise kilde. Krav fra APIs side
    private String source;

    // Mange images til en bruger
    @ManyToOne
    //Undgår uendelig løkke i ToString hvis student kalder course og omvendt
    @ToString.Exclude
    private User user;

    public SavedImage(String url, String externalId, String title, String source, User user) {
        this.url = url;
        this.externalId = externalId;
        this.title = title;
        this.source = source;
        this.user = user;
    }
}
