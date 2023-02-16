package ru.job4j.tracker.model;

import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Category {
    @NonNull
    @Getter
    private int id;
    @Getter @Setter
    @EqualsAndHashCode.Include
    private String name;
}
