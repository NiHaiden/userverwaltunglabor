package tech.niklas.userverwaltung.db;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.niklas.userverwaltung.model.Antwort;
import tech.niklas.userverwaltung.model.Frage;
import tech.niklas.userverwaltung.model.User;

import java.util.Optional;

public interface AntwortRepository extends JpaRepository<Antwort, Integer> {

    Optional<Antwort> findAntwortByFrageAndUser(Frage frage, User user);
}
