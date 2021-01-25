package domain;

import dao.DaoException;
import domain.entity.BankTransaction;
import domain.entity.Card;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;
import org.example.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import dao.DaoHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component("atmBean")
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    @Autowired
    private DaoHandler daoHandler;


    public Card searchCard(String cardNumber) throws DaoException {
        return daoHandler.searchCardByNumber(cardNumber);
    }

    public BigDecimal checkBalance(Card card) {
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.CHECKBALANCE.toString());
        daoHandler.insertBankTransaction(bankTransaction);
        return card.getBalance();
    }

    public void authentication(Card card, String pinCode) throws IncorrectPinException {
        // Аутентификация

        if (card.getTryesEnterPin() <= 0) {
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        }
        if (!card.getPinCode().equals(pinCode)) {
            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
            daoHandler.updateCard(card);
            throw new IncorrectPinException("Неверный Пин!");
        }
        card.setTryesEnterPin(3);
        daoHandler.updateCard(card);
    }

    @Transactional
    public void transferPToP(Card senderCard, Card recipientCard, BigDecimal amount)
            throws NegativeBalanceException {
        // Перевод с карты на карту

        if(senderCard.getBalance().compareTo(amount) >= 0) {
            BankTransaction senderTransaction = new BankTransaction(senderCard.getNumber(), LocalDateTime.now(), amount,
                    senderCard.getCurrency(), TransactionType.OUTTRANSFER.toString());
            BankTransaction recipientTransaction = new BankTransaction(senderTransaction, recipientCard.getNumber(),
                    TransactionType.INTRANSFER.toString());

            senderCard.setBalance(senderCard.getBalance().subtract(amount));
            recipientCard.setBalance(recipientCard.getBalance().add(amount));

            daoHandler.updateCard(senderCard);
            daoHandler.updateCard(recipientCard);
            daoHandler.insertBankTransaction(senderTransaction);
            daoHandler.insertBankTransaction(recipientTransaction);
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
    }

    public List<BankTransaction> searchTransactions(Card card) {
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.CHECKTRANSACTIONLIST.toString());
        daoHandler.insertBankTransaction(bankTransaction);
        return daoHandler.searchTransactionsByCardNumber(card.getNumber());
    }
}