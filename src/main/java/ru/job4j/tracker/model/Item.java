package ru.job4j.tracker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "items")
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Item {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @EqualsAndHashCode.Exclude
    private LocalDateTime created = LocalDateTime.now();

    public Item(String name) {
        this.name = name;
    }

    public Item(int id, String name, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, created: %s", id, name, FORMATTER.format(created));
    }
}
