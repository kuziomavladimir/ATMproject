package domain;

import dao.DaoException;
import dao.DaoHandler;
import domain.entity.BankTransaction;
import lombok.AllArgsConstructor;
import domain.entity.Card;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    private DaoHandler daoHandler;

    public Card searchCard(String cardNumber) throws DaoException {
        return daoHandler.searchCard(cardNumber);
    }

    public void authentication(Card card, String pinCode) throws IncorrectPinException, DaoException {
        // Аутентификация

        if (card.getTryesEnterPin() <= 0)
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        if (!card.getPinCode().equals(pinCode)) {
            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
            daoHandler.updateCard(card);
            throw new IncorrectPinException("Неверный Пин!");
        }
        card.setTryesEnterPin(3);
        daoHandler.updateCard(card);
    }

    public void transferPToP(Card senderCard, Card recipientCard, BigDecimal amount)
            throws NegativeBalanceException, DaoException {
        // Перевод с карты на карту или на вклад

        BankTransaction transaction = new BankTransaction(LocalDateTime.now(), amount, senderCard.getCurrency(), "Расход");
        if(senderCard.getBalance().compareTo(amount) >= 0) {
            senderCard.setBalance(senderCard.getBalance().subtract(amount));
            recipientCard.setBalance(recipientCard.getBalance().add(amount));
            daoHandler.updateCardsP2P(senderCard, recipientCard, transaction);
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
    }

    public List<BankTransaction> searchTransactions(Card card) throws DaoException {
        return daoHandler.searchTransactions(card.getNumber());
    }


}