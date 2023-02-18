package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;

import java.util.List;

public class HbmTracker implements Store {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        var isUpd = true;
        item.setId(id);
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            isUpd = false;
        } finally {
            session.close();
        }
        return isUpd;
    }

    @Override
    public boolean delete(int id) {
        var isDelete = true;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE Item WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            isDelete = false;
        } finally {
            session.close();
        }
        return isDelete;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            items = session.createQuery("from Item", Item.class)
                    .getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return items;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> items = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            items = session.createQuery("from Item WHERE name = :name", Item.class)
                    .setParameter("id", key)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return items;
    }

    @Override
    public Item findById(int id) {
        Item item = null;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            item = session.createQuery("from Item WHERE id = :id", Item.class)
                    .setParameter("id", id)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
