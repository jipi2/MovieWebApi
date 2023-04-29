package atm.webproject.movieSite.Dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto
{
    private String name;
    private String username;
    private String email;
    private String password;
}
