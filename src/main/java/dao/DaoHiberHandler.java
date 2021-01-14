package dao;

import domain.entity.BankTransaction;
import domain.entity.Card;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

@Slf4j
public class DaoHiberHandler {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public DaoHiberHandler() {
        log.info("Соединение создано? - " + String.valueOf(sessionFactory.isOpen()));
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }

    public Card searchCardByNumber(String cardNumber) throws DaoException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Card> cardList = session.createQuery("FROM Card c WHERE c.number = '" + cardNumber + "'").list();

        transaction.commit();
        if (cardList.isEmpty()) {
            throw new DaoException("Карта не найдена");
        }
        log.info("Карта найдена");
        session.close();
        return cardList.get(0);
    }

    public void updateCard(Card card) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Card beforeUpdatedCard = session.get(Card.class, card.getCardId());
        beforeUpdatedCard.setBalance(card.getBalance());
        beforeUpdatedCard.setTryesEnterPin(card.getTryesEnterPin());
        session.update(beforeUpdatedCard);

        transaction.commit();
        log.info("Карта обновлена в БД");
        session.close();
    }

    public void updateTwoCardsWithTransfer(Card card1, Card card2, BankTransaction bankTransaction1, BankTransaction bankTransaction2) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Card beforeUpdatedCard1 = session.get(Card.class, card1.getCardId());
        beforeUpdatedCard1.setBalance(card1.getBalance());
        session.update(beforeUpdatedCard1);

        Card beforeUpdatedCard2 = session.get(Card.class, card2.getCardId());
        beforeUpdatedCard2.setBalance(card2.getBalance());
        session.update(beforeUpdatedCard2);

        session.save(bankTransaction1);
        session.save(bankTransaction2);

        transaction.commit();
        log.info("Карты и транзакции обновлены в БД");
        session.close();
    }

    public List<BankTransaction> searchTransactionsByCardNumber(String cardNumber) throws DaoException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<BankTransaction> bankTransactionList = session.createQuery("FROM BankTransaction t WHERE t.cardNumber = '" + cardNumber + "' ORDER BY t.localDateTime").list();

        transaction.commit();
        log.info("Лист транзакций найден");
        session.close();
        return bankTransactionList;
    }

}
