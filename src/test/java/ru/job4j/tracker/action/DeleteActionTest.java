package ru.job4j.tracker.action;

import org.junit.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteActionTest {
    @Test
    public void whenExecute() throws Exception {
        Output out = new StubOutput();
        Store store = new MemTracker();
        store.add(new Item("new item"));
        DeleteAction deleteAction = new DeleteAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("0");

        deleteAction.execute(input, store);

        assertThat(out.toString(), is("Item is successfully deleted!"));
        assertThat(store.findAll().size(), is(0));
    }

    @Test
    public void whenExecuteWithWrongId() throws Exception {
        Output out = new StubOutput();
        Store store = new MemTracker();
        Item newItem = new Item("new item");
        store.add(newItem);
        DeleteAction deleteAction = new DeleteAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("1");

        deleteAction.execute(input, store);

        assertThat(out.toString(), is("Wrong id!"));
        assertThat(store.findAll(), is(List.of(newItem)));
    }
}