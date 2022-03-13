package tech.niklas.userverwaltung.db.init;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tech.niklas.userverwaltung.auth.Roles;
import tech.niklas.userverwaltung.db.AntwortRepository;
import tech.niklas.userverwaltung.db.FrageRepository;
import tech.niklas.userverwaltung.db.UserRepository;
import tech.niklas.userverwaltung.model.Antwort;
import tech.niklas.userverwaltung.model.Antwortmoeglichkeiten;
import tech.niklas.userverwaltung.model.Frage;
import tech.niklas.userverwaltung.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public record DatabaseInit(UserRepository userRepository,
                           FrageRepository frageRepository,
                           AntwortRepository antwortRepository,
                           PasswordEncoder passwordEncoder) implements CommandLineRunner {
    @Override
    public void run(String... args) {
        List<Frage> fragenListe = new ArrayList<>();
        if (frageRepository().count() <= 0) {
            fragenListe.addAll(List.of(
                    new Frage("Frage über Gefühle", "Wie fühlen Sie sich heute?", LocalDate.of(2022, 1, 10)),
                    new Frage("Alkohol", "Haben Sie heute schon Alkohol getrunken?", LocalDate.of(2022, 3, 31)),
                    new Frage("Drogen", "Haben Sie in Ihrem Leben schon jemals Drogen genommen?", LocalDate.of(2022, 3, 31)),
                    new Frage("Heirat", "Sind sie verheiratet?", LocalDate.of(2022, 3, 31)),
                    new Frage("Freunde", "Haben Sie Freunde?", LocalDate.of(2022, 3, 31))));

            frageRepository.saveAll(fragenListe);
        }

        if (userRepository.count() <= 0) {
            List<User> userList = new ArrayList<>(List.of(
                    new User("niklas@haiden.ch", passwordEncoder.encode("Niklas12"), Roles.ADMIN, new HashSet<>()),
                    new User("simon@wolffhardt.io", passwordEncoder.encode("Simon12"), Roles.USER, new HashSet<>()),
                    new User("fabian@weichsl.xyz", passwordEncoder.encode("Weichsl12"), Roles.USER, new HashSet<>()),
                    new User("max@max.io", passwordEncoder.encode("Max12"), Roles.USER, new HashSet<>())
            ));

            userRepository().saveAll(userList);

            if (antwortRepository.count() <= 0) {
                antwortRepository.saveAll(List.of(
                        new Antwort(null, userList.get(0), fragenListe.get(0), Antwortmoeglichkeiten.VolleZustimmung,
                                LocalDateTime.of(2021, 1, 10, 10, 0)),
                        new Antwort(null, userList.get(1), fragenListe.get(0), Antwortmoeglichkeiten.VolleZustimmung,
                                LocalDateTime.of(2021, 1, 10, 4, 0)),
                        new Antwort(null, userList.get(0), fragenListe.get(1), Antwortmoeglichkeiten.VolleZustimmung,
                                LocalDateTime.of(2022, 02, 10, 15, 19))
                ));
            }


    /*    if (userRepository.count() <= 0) {
            userRepository.saveAll(List.of(
                    new User("niklas@haiden.ch", passwordEncoder().encode("Niklas12"), Roles.USER);
            ))*/
        }
    }
}
