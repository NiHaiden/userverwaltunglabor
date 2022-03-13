package tech.niklas.userverwaltung.db;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.niklas.userverwaltung.model.User;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(@NotBlank String username);
}
