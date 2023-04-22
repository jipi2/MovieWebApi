package atm.webproject.movieSite.Dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetDto
{
    Long Id;
    String name;
    String username;
    String email;
    int numberOfPoints;

    public UserGetDto(Long id, String name, String username, String email, int numberOfPoints) {
        Id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.numberOfPoints = numberOfPoints;
    }

    public UserGetDto()
    {
        
    }

}
