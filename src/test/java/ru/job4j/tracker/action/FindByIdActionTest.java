package ru.job4j.tracker.action;

import org.junit.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindByIdActionTest {
    @Test
    public void whenExecute() throws Exception {
        var expName = "new item";
        Item exItem = new Item(expName);
        exItem.setId(0);

        Output out = new StubOutput();
        Store store = new MemTracker();
        store.add(new Item(expName));
        FindByIdAction findByIdAction = new FindByIdAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("0");

        findByIdAction.execute(input, store);

        assertThat(out.toString(), is(exItem.toString()));
    }

    @Test
    public void whenExecuteWithWrongId() throws Exception {
        var expName = "new item";
        Item exItem = new Item(expName);
        exItem.setId(0);

        Output out = new StubOutput();
        Store store = new MemTracker();
        store.add(new Item(expName));
        FindByIdAction findByIdAction = new FindByIdAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("1");

        findByIdAction.execute(input, store);

        assertThat(out.toString(), is("Wrong id! Not found"));
    }
}