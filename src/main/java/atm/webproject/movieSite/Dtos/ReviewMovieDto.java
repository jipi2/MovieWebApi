package atm.webproject.movieSite.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewMovieDto
{
    String content;
    String usernameOfUser;
    String nameOfUser;
    int numberOfStars;

    public ReviewMovieDto(String content, String usernameOfUser, String nameOfUser, int numberOfStars) {
        this.content = content;
        this.usernameOfUser = usernameOfUser;
        this.nameOfUser = nameOfUser;
        this.numberOfStars = numberOfStars;
    }

    public ReviewMovieDto(){}
}
