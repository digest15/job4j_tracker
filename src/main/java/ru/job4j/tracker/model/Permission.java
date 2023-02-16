package ru.job4j.tracker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Builder(builderMethodName = "of")
@ToString
@Getter
public class Permission {
    private int id;
    private String name;
    @Singular("rules")
    private List<String> rules;

    public static void main(String[] args) {
        Permission permission = Permission.of()
                .id(1)
                .name("sdsd")
                .rules("a")
                .rules("b")
                .build();
        System.out.println(permission);
    }
}
