package domain;

import dao.DaoException;
import dao.DaoHiberHandler;
import domain.entity.BankTransaction;
import domain.entity.Card;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;
import org.example.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//@AllArgsConstructor
@Component("atmBean")
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    @Autowired
    private DaoHiberHandler daoHiberHandler;

    public Card searchCard(String cardNumber) throws DaoException {
        return daoHiberHandler.searchCardByNumber(cardNumber);
    }

    public BigDecimal checkBalance(Card card) {
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0), card.getCurrency(), TransactionType.CHECKBALANCE);
        daoHiberHandler.insertBankTransaction(bankTransaction);
        return card.getBalance();
    }

    public void authentication(Card card, String pinCode) throws IncorrectPinException {
        // Аутентификация

        if (card.getTryesEnterPin() <= 0) {
            throw new IncorrectPinException("Пин-код ранее был введен неверно 3 раза, операция недоступна");
        }
        if (!card.getPinCode().equals(pinCode)) {
            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
            daoHiberHandler.updateCard(card);
            throw new IncorrectPinException("Неверный Пин!");
        }
        card.setTryesEnterPin(3);
        daoHiberHandler.updateCard(card);
    }

    public void transferPToP(Card senderCard, Card recipientCard, BigDecimal amount)
            throws NegativeBalanceException, DaoException {
        // Перевод с карты на карту

        if(senderCard.getBalance().compareTo(amount) >= 0) {
            BankTransaction senderTransaction = new BankTransaction(senderCard.getNumber(), LocalDateTime.now(), amount, senderCard.getCurrency(), TransactionType.OUTTRANSFER);
            BankTransaction recipientTransaction = new BankTransaction(senderTransaction, recipientCard.getNumber(), TransactionType.INTRANSFER);

            senderCard.setBalance(senderCard.getBalance().subtract(amount));
            recipientCard.setBalance(recipientCard.getBalance().add(amount));
            daoHiberHandler.updateTwoCardsWithTransfer(senderCard, recipientCard, senderTransaction, recipientTransaction);
        }
        else {
            throw new NegativeBalanceException("Введенная сумма превышает остаток на вашем счете!");
        }
    }

    public List<BankTransaction> searchTransactions(Card card) throws DaoException {
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0), card.getCurrency(), TransactionType.CHECKTRANSACTIONLIST);
        daoHiberHandler.insertBankTransaction(bankTransaction);
        return daoHiberHandler.searchTransactionsByCardNumber(card.getNumber());
    }
}