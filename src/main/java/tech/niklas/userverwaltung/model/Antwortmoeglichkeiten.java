package tech.niklas.userverwaltung.model;

import lombok.Getter;

@Getter
public enum Antwortmoeglichkeiten {
    VolleZustimmung("volle Zustimmung"), TeilweiseZustimmung("teilweise Zustimmung"),
    KeineZustimmung("keine Zustimmung");

    private final String antwort;

    Antwortmoeglichkeiten(String antwort) {
        this.antwort = antwort;
    }

}