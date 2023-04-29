package atm.webproject.movieSite.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfoDto
{
    String name;
    String description;
    String link;
    String pathImage;
    double rating;

}
