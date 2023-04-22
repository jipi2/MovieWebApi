package atm.webproject.movieSite.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(
        name="Users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_username_unique", columnNames = "username")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    public User(Long id, String name, String username, String email, String password, int numberOfPoints) {
        Id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.numberOfPoints = numberOfPoints;
    }

    public User(String name, String username, String email, String password, int numberOfPoints) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.numberOfPoints = numberOfPoints;
    }
    public User() {
    }

    @Column(
            name = "name",
            nullable = true,
            columnDefinition = "TEXT"
    )
    String name;

    @Column(
            name = "username",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String username;

    @Column(
            name = "email",
            nullable = true,
            columnDefinition = "TEXT"
    )
    String email;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    String password;

    @Column(
            name = "numberOfPoints",
            nullable = true,
            columnDefinition = "INT"
    )
    int numberOfPoints;

    @ManyToMany
    @JoinTable(
            name="user_movie",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> savedMovies = new HashSet<>();

    public void addMovieToWatchList(Movie movie)
    {
        savedMovies.add(movie);
    }


}
