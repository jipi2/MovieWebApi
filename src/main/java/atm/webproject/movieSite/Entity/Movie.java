package atm.webproject.movieSite.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 name="movies",
 uniqueConstraints ={
         @UniqueConstraint(name="movie_name_unique", columnNames = "name")
 }
)
public class Movie {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(
            name = "name",
            nullable = false
    )
    String name;

    @Column(
            name = "rating",
            nullable = false
    )
    int rating;

    @Column(
            name = "pathImage",
            nullable = false
    )
    String pathImage;

    @Column(
            name = "trailer",
            nullable = false
    )
    String trailer;

    @Column(
            name = "nameActors",
            nullable = true
    )
    String nameActors;

    @Column(
            name = "gender",
            nullable = false
    )
    String gender;

    @JsonIgnore
    @ManyToMany(mappedBy = "savedMovies")
    private Set<User> users = new HashSet<>();

    public Movie(Long id, String name, int rating, String pathImage, String trailer,String nameActors, String gender) {
        Id = id;
        this.name = name;
        this.rating = rating;
        this.pathImage = pathImage;
        this.trailer = trailer;
        this.nameActors = nameActors;
        this.gender = gender;
    }

    public Movie(String name, int rating, String pathImage, String trailer, String nameActors, String gender) {
        this.name = name;
        this.rating = rating;
        this.pathImage = pathImage;
        this.trailer = trailer;
        this.nameActors = nameActors;
        this.gender=gender;
    }

    public Movie(String name, int rating, String pathImage, String trailer, String gender) {
        this.name = name;
        this.rating = rating;
        this.pathImage = pathImage;
        this.trailer = trailer;
        this.gender = gender;
    }

    public Movie() {

    }
}
