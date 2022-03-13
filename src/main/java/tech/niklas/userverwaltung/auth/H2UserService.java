package tech.niklas.userverwaltung.auth;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.niklas.userverwaltung.db.UserRepository;

@Service
public class H2UserService implements UserDetailsService {

    UserRepository userRepository;

    public H2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username).orElseThrow(() -> {
            throw new IllegalArgumentException("User could not be found by E-Mail!");
        });

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles().name())
                .build();
    }
}
