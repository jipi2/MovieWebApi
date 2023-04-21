package atm.webproject.movieSite.Configuration;

import atm.webproject.movieSite.Entity.User;
import atm.webproject.movieSite.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfiguration {
//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository repository)
//    {
//        return args -> {
//                User marian = new User(
//                       "Marian",
//                        "Marian_e_tare",
//                        "EmailMarian",
//                        "parolaMarian",
//                        0
//                );
//
//            User costel = new User(
//                    "Costel",
//                    "Costel_e_tare",
//                    "EmailCostel",
//                    "parolaCostel",
//                    0
//            );
//
//            repository.saveAll(List.of(marian, costel));
//        };
//    }

}
