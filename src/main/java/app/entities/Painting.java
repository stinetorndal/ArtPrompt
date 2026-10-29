package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Painting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;

    // Skal bruges til at vise thumbnail i frontend
    private String url;
    private String artist;
    private String title;
    private int year;

    public Painting(Long id, String externalId, String url, String artist, String title, int year) {
        this.id = id;
        this.externalId = externalId;
        this.url = url;
        this.artist = artist;
        this.title = title;
        this.year = year;
    }
}