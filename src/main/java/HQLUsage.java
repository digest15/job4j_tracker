import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.tracker.model.Item;


public class HQLUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            Session session = sf.openSession();

            /* Select */
            Query<Item> query = session.createQuery("from Item",  Item.class);
            for (Object st : query.list()) {
                System.out.println(st);
            }
            query = session.createQuery("from Item", Item.class);

            /* Select with parameter */
            query = session.createQuery(
                    "from Item as i where i.id = 3", Item.class);
            System.out.println(query.uniqueResult());

            /* Select with condition */
            query = session.createQuery(
                    "from Item as i where i.id = :fId", Item.class);
            query.setParameter("fId", 3);
            System.out.println(query.uniqueResult());

            /* Update */
            try {
                session.beginTransaction();
                session.createQuery(
                                "UPDATE Item SET name = :fName WHERE id = :fId")
                        .setParameter("fName", "new name")
                        .setParameter("fId", 3)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }

            /* Delete */
            try {
                session.beginTransaction();
                session.createQuery(
                                "DELETE Item WHERE id = :fId")
                        .setParameter("fId", 3)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }

            session.close();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
