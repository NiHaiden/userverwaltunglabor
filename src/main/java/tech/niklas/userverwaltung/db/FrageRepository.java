package tech.niklas.userverwaltung.db;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.niklas.userverwaltung.model.Antwortmoeglichkeiten;
import tech.niklas.userverwaltung.model.Frage;
import tech.niklas.userverwaltung.model.User;

import java.time.LocalDate;
import java.util.List;

public interface FrageRepository extends JpaRepository<Frage, Integer> {
    @Query("SELECT f FROM Frage f " +
            "WHERE NOT EXISTS (SELECT a FROM Antwort a WHERE a.frage = f AND a.user = :user) AND f.ablaufdatum > :currentDate")
    List<Frage> getUnansweredQuestionsByUser(User user, LocalDate currentDate);

    @Query(value = "SELECT count(a.gewaehlteOption) FROM Antwort a INNER JOIN Frage f on a.frage.id=f.id " +
            "WHERE f.id=:frageId AND a.gewaehlteOption=:antwortOption " +
            "GROUP BY f.id, a.gewaehlteOption, f.ueberschrift, f.ablaufdatum " +
            "ORDER BY f.id, a.gewaehlteOption")
    Integer amountOfAnswersByIdAndType(@Param("frageId") Integer frageId, @Param("antwortOption") Antwortmoeglichkeiten antwort);
}
