package ru.job4j.tracker.action;

import ru.job4j.tracker.output.Output;

import java.util.StringJoiner;

public class StubOutput implements Output {

    private StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

    @Override
    public void println(Object obj) {
        stringJoiner.add(obj.toString());
    }

    @Override
    public String toString() {
        return stringJoiner.toString();
    }
}
