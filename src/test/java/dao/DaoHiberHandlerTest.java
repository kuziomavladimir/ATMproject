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
    void testSessionInsert() {
        User user = new User("Alala", "Ivasanova", new Date(), "a78lka@yandex.com" );


//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(user);

        transaction.commit();
        session.close();
    }

    @Test
    void testSessionRead() {
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<User> userList = session.createQuery("FROM User").list();

        transaction.commit();
        session.close();

        for(User user: userList) {
            System.out.println(user);
        }
    }

    @Test
    void testSessionUpdate() {
        int iserId = 37;
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = session.get(User.class, iserId);    // поиск юзера по id

        user.setName("Andrey");
        user.setSurname("Gugu");
        user.seteMail("guna@mail.ru");

        session.update(user);
        transaction.commit();
        session.close();

        System.out.println(user);
    }

    @Test
    void testSessionDelete() {
        int iserId = 37;
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User user = session.get(User.class, iserId);    // поиск юзера по id
        session.delete(user);

        transaction.commit();
        session.close();
    }


}