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

public class FindByNameActionTest {
    @Test
    public void whenExecute() throws Exception {
        var expName = "new item";
        Item item1 = new Item(expName);
        item1.setId(0);
        Item item2 = new Item(expName);
        item2.setId(1);

        Output out = new StubOutput();
        Store store = new MemTracker();
        store.add(item1);
        store.add(item2);
        FindByNameAction findByNameAction = new FindByNameAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn(expName);

        findByNameAction.execute(input, store);

        var expOut = item1.toString()
                + System.lineSeparator()
                + item2.toString();
        assertThat(out.toString(), is(expOut));
    }

    @Test
    public void whenExecuteWithWrongName() throws Exception {
        var expName = "new item";

        Output out = new StubOutput();
        Store store = new MemTracker();
        store.add(new Item(expName));
        FindByNameAction findByNameAction = new FindByNameAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("1");

        findByNameAction.execute(input, store);

        assertThat(out.toString(), is(""));
    }
}