package tech.niklas.userverwaltung.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "frage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Frage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Die Überschrift darf nicht leer sein!")
    @Length(max = 20, message = "Fragenüberschrift darf nicht über 20 Zeichen lang sein!")
    @Column(name = "ueberschrift", length = 20)
    private String ueberschrift;

    @NotBlank(message = "Der Fragentext darf nicht leer sein!")
    @Length(max = 200, message = "Fragetext darf nicht über 200 Zeichen lang sein!")
    @Column(name = "fragetext", length = 200)
    private String fragetext;

    @Column(name = "ablaufdatum")
    @NotNull(message = "Sie müssen ein Datum angeben!")
    private LocalDate ablaufdatum;

    @OneToMany(mappedBy = "frage")
    @ToString.Exclude
    private Set<Antwort> antworten = new HashSet<>();

    public Frage(String ueberschrift, String fragetext, LocalDate ablaufdatum) {
        this.ueberschrift = ueberschrift;
        this.fragetext = fragetext;
        this.ablaufdatum = ablaufdatum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Frage frage = (Frage) o;
        return id != null && Objects.equals(id, frage.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
