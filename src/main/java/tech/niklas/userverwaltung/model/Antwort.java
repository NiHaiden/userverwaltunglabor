package tech.niklas.userverwaltung.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "antwort", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ant_mit_id", "ant_frage_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Antwort implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ant_id")
    @EqualsAndHashCode.Include
    private Integer ant_id;


    @ManyToOne
    @JoinColumn(name = "ant_mit_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ant_frage_id", nullable = false)
    private Frage frage;


    @Column(name = "antwort")
    @NotNull(message = "Sie müssen eine Antwort auswählen!")
    private Antwortmoeglichkeiten gewaehlteOption;

    @Column(name = "timestamp")
    @PastOrPresent
    private LocalDateTime timestamp;
}
