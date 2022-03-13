package tech.niklas.userverwaltung.db;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.niklas.userverwaltung.model.Antwort;

public interface AntwortRepository extends JpaRepository<Antwort, Integer> {

}
