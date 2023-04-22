package atm.webproject.movieSite.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(
     name="reviews"
)
public class Review
{
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(
            name="content",
            nullable = true
    )
    String content;

    @Column(
            name="numberOfStars",
            nullable = false
    )
    int numberOfStars;

    @Column(
            name = "isVerified",
            nullable = false
    )
    boolean isVerified;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "Id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="movie_id", referencedColumnName = "Id")
    private Movie movie;

    public Review(String content, int numberOfStars) {
        this.content = content;
        this.numberOfStars = numberOfStars;
        this.isVerified = false;
    }

    public Review()
    {

    }


    public void assignUser(User user)
    {
        this.user = user;
    }

    public void assignMovie(Movie movie)
    {
        this.movie = movie;
    }
}
