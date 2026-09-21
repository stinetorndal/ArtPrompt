package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@ToString

public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private LocalDate createdAt;

    // Mange noter til en bruger
    @ManyToOne
    //Undgår uendelig løkke i ToString hvis student kalder course og omvendt
    @ToString.Exclude
    private User user;
    }
