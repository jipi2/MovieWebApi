package atm.webproject.movieSite.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto
{
    String content;
    int numberOfStars;

    public ReviewCreateDto(String content, int numberOfStars) {
        this.content = content;
        this.numberOfStars = numberOfStars;
    }

    public ReviewCreateDto() {
    }
}
