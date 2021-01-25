package ui;

import dao.DaoException;
import domain.ATM;
import domain.customExeptions.IncorrectPinException;
import domain.customExeptions.NegativeBalanceException;
import domain.entity.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/ATM")
public class ATMController {

    @Autowired
    private ATM atm;
    private Card card;      //todo: Стоит ли тут вообще работать с бизнес-сущностью, или лучше передать строку для поиска в бизнес-класс ATM
    private String answerMessage;

    @GetMapping()
    public String startPage(@ModelAttribute("number") String number, @ModelAttribute("pin") String pin) {
        card = null;        //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
        return "StartPage";
    }

    @PostMapping()
    @Transactional
    public String startPagePost(@ModelAttribute("number") String number, @ModelAttribute("pin") String pin) {

        log.info("Введённый номер карты и пин:" + number + "\t" + pin);

        try {
            card = atm.searchCard(number);
        } catch (DaoException e) {
            card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
            log.info(e.toString());
            answerMessage = e.getMessage();
            return "redirect:/ATM/answer?answertext=" + answerMessage;
        }

        try {
            atm.authentication(card, pin);
        } catch (IncorrectPinException e) {
            card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
            log.info(e.toString());
            answerMessage = e.getMessage();
            return "redirect:/ATM/answer?answertext=" + answerMessage;
        }

        log.info("Пост запрос на аутентификацию проведен");
        return "redirect:/ATM/mainmenu";
    }

    @GetMapping("/answer")
    public String errorPage(@RequestParam(value = "answertext", required = false) String answertext, Model model) {

        model.addAttribute("message", answertext);
        card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
        return "AnswerPage";
    }

    @GetMapping("/mainmenu")
    public String mainMenuPage() {
        return "MainMenu";
    }

    @GetMapping("/balance")
    public String balancePage(Model model) {
        model.addAttribute("message", atm.checkBalance(card));
        card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
        return "BalancePage";
    }

    @GetMapping("/transactions")
    public String transactionsPage(Model model) {
        model.addAttribute("transactions", atm.searchTransactionsStory(card));
        card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
        return "TransactionsPage";
    }

    @GetMapping("/transfer")
    public String doTransferGet(@ModelAttribute("resipientNumber") String resipientNumber,
                                @ModelAttribute("amount") String amount) {
        return "TransferPage";
    }

    @Transactional
    @PostMapping("/transfer")
    public String doTransferPost(@ModelAttribute("resipientNumber") String resipientNumber,
                                 @ModelAttribute("amount") String amount) {

        log.info("Введённый номер карты получателя: \t" + resipientNumber + ",\tСумма: " + amount);

        try {
            atm.transferPToP(card, resipientNumber, amount);
        } catch (DaoException|NegativeBalanceException e) {
            card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
            log.info(e.toString());
            answerMessage = e.getMessage();
            return "redirect:/ATM/answer?answertext=" + answerMessage;
        }

        card = null;    //todo: Уничтожить карту для безопасности (продумать как повысить безопасность)
        log.info("Пост запрос на перевод проведен");
        answerMessage = "Successfully!!!";
        return "redirect:/ATM/answer?answertext=" + answerMessage;
    }
}
