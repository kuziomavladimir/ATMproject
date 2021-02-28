package services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import repository.UsersRepository;
import services.customExeptions.CardNotFoundException;
import org.springframework.stereotype.Service;
import repository.BankTransactionsRepository;
import repository.CardsRepository;
import services.customExeptions.ViolationUniquenessException;
import services.entity.BankTransaction;
import services.entity.Card;
import services.customExeptions.IncorrectPinException;
import services.customExeptions.NegativeBalanceException;
import org.example.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import services.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ATM {
    // Основной класс с бизнесс-методами, моделирует функции банкомата

    private final CardsRepository cardsRepository;
    private final BankTransactionsRepository bankTransactionsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    private MailSender mailSender;

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

    @Transactional(rollbackFor=Exception.class)
    public User createOrFindNewUser(User incomingUser) throws ViolationUniquenessException {
        // Поиск, либо создание нового пользователя в бд

        User user;
        try {
            user = usersRepository.findByUserNameAndSurnameAndBirthdayAndEmail(incomingUser.getUserName(), incomingUser.getSurname(),
                    incomingUser.getBirthday(), incomingUser.getEmail()).orElseGet(() -> {
                usersRepository.save(incomingUser);
                log.info("Новый пользователь добавлен в БД");
                return incomingUser;
            });
        } catch (DataIntegrityViolationException e) {
            throw new ViolationUniquenessException("E-mail address is already used, please change");
        }
        return user;
    }

    @Transactional(rollbackFor=Exception.class)
    public void createNewCard(User incomingUser) throws ViolationUniquenessException {
        // Создание новой карты в бд

        User user = createOrFindNewUser(incomingUser);
        String uniqueCardNumber = UUID.randomUUID().toString().replaceAll("[^0-9]", "0").substring(0, 16);
        String randomPinCode = Double.toString(Math.random() * 1000).replaceAll("[^0-9]", "").substring(0, 4);
        Card card = new Card(user.getUserId(), uniqueCardNumber, randomPinCode, "RUR", BigDecimal.valueOf(0));
        log.info(card.toString());

        try {
            cardsRepository.save(card);
        } catch (DataIntegrityViolationException e) {
            throw new ViolationUniquenessException("Card number uniqueness error, please repeat");
        }

        BankTransaction bankTransaction = new BankTransaction(card.getNumber(), LocalDateTime.now(), BigDecimal.valueOf(0),
                card.getCurrency(), TransactionType.OPENCARD);
        bankTransactionsRepository.save(bankTransaction);

        String message = "Your card number:\t" + card.getNumber() + "\n" + "Pin code:\t" + card.getPinCode();
        mailSender.send(user.getEmail(), "Opening a new card", message);
    }


}