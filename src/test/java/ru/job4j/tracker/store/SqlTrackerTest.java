package ru.job4j.tracker.store;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SqlTrackerTest {
    private static Connection connection;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader().getResourceAsStream("test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSave_ItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void whenReplace_ItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("item1"));
        item.setName("item2");
        boolean result = tracker.replace(item.getId(), item);
        assertTrue(result);
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void whenDelete_ItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("item1"));
        boolean result = tracker.delete(item.getId());
        assertTrue(result);
        assertThat(tracker.findById(item.getId()), is(nullValue()));
    }

    @Test
    public void whenFindAll_ResultMustContainAllAddedItems() {
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item("item1"),
                new Item("item2")
        );
        items.forEach(tracker::add);
        assertThat(tracker.findAll(), is(items));
    }

    @Test
    public void whenFindByName_ResultMustContainAllAddedItems() {
        String name = "item1";
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item(name),
                new Item(name)
        );
        items.forEach(tracker::add);
        assertThat(tracker.findByName(name), is(items));
    }
}