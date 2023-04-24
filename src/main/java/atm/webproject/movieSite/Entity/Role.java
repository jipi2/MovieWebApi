package atm.webproject.movieSite.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name="roles",
        uniqueConstraints = {
                @UniqueConstraint(name="role_name_unique", columnNames = "roleName")
        }
)
public class Role
{
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(
            name = "roleName",
            nullable = false
    )
    String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "savedRoles")
    private Set<User> users = new HashSet<>();
}
