package ru.job4j.tracker.store;

import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class SqlTracker implements Store {
    private Connection connection;

    public SqlTracker() {
        init();
    }

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
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

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into items(name, created) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            ps.execute();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    item.setId(keys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        return executeUpdate(
                "update items set name = ?, created = ? where id = ?",
                ps -> {
                        ps.setString(1, item.getName());
                        ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
                        ps.setInt(3, id);
                    }
                );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate(
                "delete from items where id = ?",
                ps -> ps.setInt(1, id)
        );
    }

    @Override
    public List<Item> findAll() {
        return executeSelect(
                "select id, name, created from items",
                ps -> {},
                this::createItem
        );
    }

    @Override
    public List<Item> findByName(String key) {
        return executeSelect(
                "select id, name, created from items where name = ?",
                ps -> ps.setString(1, key),
                this::createItem
        );
    }

    @Override
    public Item findById(int id) {
        List<Item> items = executeSelect(
                "select id, name, created from items where id = ?",
                ps -> ps.setInt(1, id),
                this::createItem
        );
        return items.size() > 0
                ? items.get(0)
                : null;
    }

    private boolean executeUpdate(String sql,
                                  Customizer customizer) {
        boolean result = false;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            customizer.apply(ps);
            result = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Item> executeSelect(String sql,
                                     Customizer customizer,
                                     Function<ResultSet, Item> wrapper) {
        var items = new ArrayList<Item>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            customizer.apply(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(wrapper.apply(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    private Item createItem(ResultSet rs) {
        Item item;
        try {
            item = new Item(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getTimestamp("created").toLocalDateTime()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    @FunctionalInterface
    private interface Customizer {
        void config(PreparedStatement ps) throws Exception;

        default PreparedStatement apply(PreparedStatement ps) throws Exception {
            config(ps);
            return ps;
        }
    }
}
