package atm.webproject.movieSite.Dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto
{
    private String username;
    private String password;
}
