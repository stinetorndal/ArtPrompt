package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@ToString
public class User {
    @Id
    //DB opretter selv id og styrer det via egen tæller. Svarer til SERIAL
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    //Relation til User-objekt så JAP/Hibernate ved, note hører til bruger
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    //Relation til User-objekt så JAP/Hibernate ved, note hører til bruger
    private List<SavedImage> images = new ArrayList<>();

}