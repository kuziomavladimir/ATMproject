package services;

import dao.DaoException;
import org.springframework.stereotype.Service;
import services.entity.BankTransaction;
import services.entity.Card;
import services.customExeptions.IncorrectPinException;
import services.customExeptions.NegativeBalanceException;
import org.example.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import dao.DaoHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    @Autowired
    private DaoHandler daoHandler;

    public Card searchCard(String cardNumber) throws DaoException {
        // Поиск карты по номеру
        return daoHandler.searchCardByNumber(cardNumber);
    }

    public void authentication(String cardNumber, String pinCode) throws DaoException, IncorrectPinException {
        // Аутентификация
        Card card = searchCard(cardNumber);
        if (card.getTryesEnterPin() <= 0) {
            throw new IncorrectPinException("The PIN code was previously entered incorrectly 3 times, the operation is not available");
        }
        if (!card.getPinCode().equals(pinCode)) {
            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
            daoHandler.updateCard(card);
            throw new IncorrectPinException("Incorrect Pin-code");
        }
        card.setTryesEnterPin(3);
        daoHandler.updateCard(card);
    }

    public BigDecimal checkBalance(String cardNumber, String pinCode) throws DaoException, IncorrectPinException{
        // Проверка баланса
        Card card = searchCard(cardNumber);
        authentication(cardNumber, pinCode);
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.CHECKBALANCE.toString());
        daoHandler.insertBankTransaction(bankTransaction);
        return card.getBalance();
    }

    public List<BankTransaction> searchTransactionsStory(String cardNumber, String pinCode) throws DaoException, IncorrectPinException{
        // Проверка истории операций
        Card card = searchCard(cardNumber);
        authentication(cardNumber, pinCode);
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.CHECKTRANSACTIONLIST.toString());
        daoHandler.insertBankTransaction(bankTransaction);
        return daoHandler.searchTransactionsByCardNumber(card.getNumber());
    }

    @Transactional
    public void transferPToP(String senderCardNumber, String pinCode, String recipientCardNumber, String amount)
            throws DaoException, IncorrectPinException, NegativeBalanceException {
        // Перевод с карты на карту

        Card senderCard = searchCard(senderCardNumber);
        authentication(senderCardNumber, pinCode);

        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new NegativeBalanceException("Incorrect amount format");
        }

        Card recipientCard = searchCard(recipientCardNumber);

        if(senderCard.getBalance().compareTo(bigDecimal) >= 0) {
            BankTransaction senderTransaction = new BankTransaction(senderCard.getNumber(), LocalDateTime.now(), bigDecimal,
                    senderCard.getCurrency(), TransactionType.OUTTRANSFER.toString());
            BankTransaction recipientTransaction = new BankTransaction(senderTransaction, recipientCard.getNumber(),
                    TransactionType.INTRANSFER.toString());

            senderCard.setBalance(senderCard.getBalance().subtract(bigDecimal));
            recipientCard.setBalance(recipientCard.getBalance().add(bigDecimal));

            daoHandler.updateCard(senderCard);
            daoHandler.updateCard(recipientCard);   //todo: Исправить! Пин-код карты получателя так же апдейтится!!!
            daoHandler.insertBankTransaction(senderTransaction);
            daoHandler.insertBankTransaction(recipientTransaction);
        }
        else {
            throw new NegativeBalanceException("The amount entered exceeds your account balance!");
        }
    }
}