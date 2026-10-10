package app.entities;

import app.routes.Role;
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
@ToString(exclude = {"password", "notes", "images"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SavedImage> images = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        // role bliver automatisk USER via standardværdien ovenfor
    }
}