package dao;

import domain.entity.BankTransaction;
import domain.entity.Card;
import domain.entity.User;
import lombok.extern.slf4j.Slf4j;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DaoHiberHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void sessionInsertTest() {
        User user = new User(0, "Alala", "Ivanova", LocalDate.now(), "a788lka@yandex.com" );

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
            log.info(user.toString());
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

        log.info(user.toString());
    }

    @Test
    void sessionDeleteTest() {
        int userId = 43;
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

    @Test
    void searchCardListTest() {
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Card> cardList = session.createQuery("FROM Card c ORDER BY c.id").list();    //todo: выучить язык HQL, и критерия

        transaction.commit();
        session.close();
        sessionFactory.close();

        for(Card card: cardList) {
            log.info(card.toString());
        }
    }

    @Test
    void searchCardByNumberTest() throws DaoException {
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

//        Card card = session.get(Card.class, 4);
        List<Card> cardList = session.createQuery("FROM Card c WHERE c.number = '4276600022222222'").list();

        if (cardList.isEmpty()) {
            throw new DaoException("Карта не найдена");
        }

        transaction.commit();
        session.close();
        sessionFactory.close();

        log.info(cardList.get(0).toString());
    }

    @Test
    public void updateCardTest() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Card card = new Card(10,3, "5469600088888888", "1100", "RUR", new BigDecimal(1000), 1);

        Card beforeUpdateCard = session.get(Card.class, card.getCardId());
        beforeUpdateCard.setBalance(card.getBalance());
        beforeUpdateCard.setTryesEnterPin(card.getTryesEnterPin());
        session.update(beforeUpdateCard);

        transaction.commit();
        session.close();
    }

    @Test
    public void updateTwoCardsWithTransfer() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Card> cardList1 = session.createQuery("FROM Card c WHERE c.number = '4276600022222222'").list();
        Card card1 = cardList1.get(0);
        List<Card> cardList2 = session.createQuery("FROM Card c WHERE c.number = '4276600011111111'").list();
        Card card2 = cardList2.get(0);
        card1.setBalance(new BigDecimal(100000));
        card2.setBalance(new BigDecimal(105000));

        Card beforeUpdatedCard1 = session.get(Card.class, card1.getCardId());
        beforeUpdatedCard1.setBalance(card1.getBalance());
        session.update(beforeUpdatedCard1);
        Card beforeUpdatedCard2 = session.get(Card.class, card2.getCardId());
        beforeUpdatedCard2.setBalance(card2.getBalance());
        session.update(beforeUpdatedCard2);

        transaction.commit();
        log.info("Карта обновлена в БД");
        session.close();
    }

    @Test
    public void updateTransactionTest() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();  // Тяжеловесный объект, лучше вынести на уровень поля класса и инициализировать в конструкторе???
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

//        List<BankTransaction> transactionList = session.createQuery("FROM BankTransaction").list();
//        for (BankTransaction b: transactionList)
//            log.info(b.toString);

        List<Card> cardList1 = session.createQuery("FROM Card c WHERE c.number = '4276600022222222'").list();
        Card card1 = cardList1.get(0);
        List<Card> cardList2 = session.createQuery("FROM Card c WHERE c.number = '4276600011111111'").list();
        Card card2 = cardList2.get(0);
//        card1.setBalance(new BigDecimal(100000));
//        card2.setBalance(new BigDecimal(105000));

        BankTransaction bankTransaction1 = new BankTransaction(card1.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(78789.56), "RUR", "Расход");
        BankTransaction bankTransaction2 = new BankTransaction(bankTransaction1, card2.getNumber(), "Приход");

        session.save(bankTransaction1);
        session.save(bankTransaction2);

        transaction.commit();
        session.close();
    }

}