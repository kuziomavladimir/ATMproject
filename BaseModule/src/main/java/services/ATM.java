package services;

import services.customExeptions.CardNotFoundException;
import org.springframework.stereotype.Service;
import repository.BankTransactionsRepository;
import repository.CardsRepository;
import services.entity.BankTransaction;
import services.entity.Card;
import services.customExeptions.IncorrectPinException;
import services.customExeptions.NegativeBalanceException;
import org.example.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата, входит в доменную модель

    @Autowired
    private CardsRepository cardsRepository;
    @Autowired
    private BankTransactionsRepository bankTransactionsRepository;


    public Card searchCard(String cardNumber) throws CardNotFoundException {
        // Поиск карты по номеру
        return cardsRepository.findByNumber(cardNumber).orElseThrow(() -> new CardNotFoundException("Card not found in Data base"));
    }

    public void authentication(String cardNumber, String pinCode) throws CardNotFoundException, IncorrectPinException {
        // Аутентификация
        Card card = searchCard(cardNumber);
        if (card.getTryesEnterPin() <= 0) {
            throw new IncorrectPinException("The PIN code was previously entered incorrectly 3 times, the operation is not available");
        }
        if (!card.getPinCode().equals(pinCode)) {
            card.setTryesEnterPin(card.getTryesEnterPin() - 1);
            cardsRepository.save(card);
            throw new IncorrectPinException("Incorrect Pin-code");
        }
        card.setTryesEnterPin(3);
        cardsRepository.save(card);
    }

    public BigDecimal checkBalance(String cardNumber, String pinCode) throws CardNotFoundException, IncorrectPinException{
        // Проверка баланса
        Card card = searchCard(cardNumber);
        authentication(cardNumber, pinCode);
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.CHECKBALANCE);
        bankTransactionsRepository.save(bankTransaction);
        return card.getBalance();
    }

    public List<BankTransaction> searchTransactionsStory(String cardNumber, String pinCode) throws CardNotFoundException, IncorrectPinException{
        // Проверка истории операций
        Card card = searchCard(cardNumber);
        authentication(cardNumber, pinCode);
        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.CHECKTRANSACTIONLIST);
        bankTransactionsRepository.save(bankTransaction);
        return bankTransactionsRepository.findAllByCardNumberOrderByDateTime(card.getNumber());
    }

    @Transactional
    public void transferPToP(String senderCardNumber, String pinCode, String recipientCardNumber, String amount)
            throws CardNotFoundException, IncorrectPinException, NegativeBalanceException {
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
                    senderCard.getCurrency(), TransactionType.OUTTRANSFER);
            BankTransaction recipientTransaction = new BankTransaction(senderTransaction, recipientCard.getNumber(),
                    TransactionType.INTRANSFER);

            senderCard.setBalance(senderCard.getBalance().subtract(bigDecimal));
            recipientCard.setBalance(recipientCard.getBalance().add(bigDecimal));

            cardsRepository.save(senderCard);
            cardsRepository.save(recipientCard);
            bankTransactionsRepository.save(senderTransaction);
            bankTransactionsRepository.save(recipientTransaction);
        }
        else {
            throw new NegativeBalanceException("The amount entered exceeds your account balance!");
        }
    }
}