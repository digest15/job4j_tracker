package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

import java.util.stream.IntStream;

public class GenerateManyAction implements UserAction {

    private final Output out;

    public GenerateManyAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "=== Generate many a new Item ====";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        int count = input.askInt("Enter how many: ");
        IntStream.range(0, count).forEach(i -> {
            Item item = new Item("Item_" + i);
            tracker.add(item);
        });

        out.println("Items is successfully generated!");
        return true;
    }
}
