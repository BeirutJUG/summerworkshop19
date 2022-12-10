package example;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;

import example.model.Address;
import example.model.Category;
import example.model.Course;
import example.model.Purchase;
import example.model.User;

public class TestHibernateExample {

    @Test
    public void addUserPurchase() {
        SessionFactory sessionFactory = createSessionFactory();
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Category category = new Category("Java");
            session.save(category);

            Course course = new Course();
            course.setTitle("Hibernate 101");
            course.setCategory(category);
            course.setPrice(new BigDecimal(20));
            session.save(course);

            User user = new User();
            user.setUserName("Duke");
            user.setAddress(new Address("Bean Street", "Duke City"));
            user.setBillingAddress(new Address("Bean Street", "Duke City"));

            Purchase purchase = new Purchase();
            purchase.setCourse(course);
            purchase.setPurchasedOn(new Date());
            user.addPurchase(purchase);

            session.save(user); // will cascade insert of purchases

            transaction.commit();

            Transaction transaction2 = session.beginTransaction();
            CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
            criteriaQuery.from(User.class);
            List<User> list = session.createQuery(criteriaQuery).list();

            assertEquals(1, list.size());

            User fetchedUser = list.get(0);
            assertEquals(1, fetchedUser.getPurchases().size());

            Purchase userPurchase = fetchedUser.getPurchases().iterator().next();
            assertEquals("Hibernate 101", userPurchase.getCourse().getTitle());
            assertEquals("Java", userPurchase.getCourse().getCategory().getName());
            transaction2.commit();
        }
    }

    private SessionFactory createSessionFactory() {
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();

        serviceRegistryBuilder.applySetting("hibernate.connection.driver_class", "org.h2.Driver")
                              .applySetting("hibernate.connection.url", "jdbc:h2:mem:test")
                              .applySetting("hibernate.connection.username", "sa")
                              .applySetting("hibernate.format_sql", "true")
                              .applySetting("hibernate.use_sql_comments", "true")
                              .applySetting("hibernate.hbm2ddl.auto", "create-drop");

        StandardServiceRegistry serviceRegistry = serviceRegistryBuilder.build();

        MetadataSources metadataSources = new MetadataSources();
        metadataSources.addAnnotatedClass(User.class)
                       .addAnnotatedClass(Course.class)
                       .addAnnotatedClass(Category.class)
                       .addAnnotatedClass(Purchase.class);
        Metadata metadata = metadataSources.buildMetadata(serviceRegistry);
        return metadata.buildSessionFactory();
    }
}
