package dao;

import domain.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DaoHiberHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void sessionInsertTest() {
        User user = new User("Alala", "Ivanova", new Date(), "a788lka@yandex.com" );

//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(user);

        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    @Test
    void sessionReadTest() {
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<User> userList = session.createQuery("FROM User u ORDER BY u.id").list();    //todo: выучить язык HQL, и критерия

        transaction.commit();
        session.close();
        sessionFactory.close();

        for(User user: userList) {
            System.out.println(user);
        }
    }

    @Test
    void sessionUpdateTest() {
        int userId = 41;
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = session.get(User.class, userId);    // поиск юзера по id

        user.setName("Andrey");
        user.setSurname("Gugu");
        user.seteMail("guna@mail.ru");

        session.update(user);

        transaction.commit();
        session.close();
        sessionFactory.close();

        System.out.println(user);
    }

    @Test
    void sessionDeleteTest() {
        int userId = 42;
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = session.get(User.class, userId);    // поиск юзера по id
        session.delete(user);

        transaction.commit();
        session.close();
        sessionFactory.close();
    }


}