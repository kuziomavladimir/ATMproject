package controllers;

import dao.DaoException;
import org.springframework.validation.BindingResult;
import services.ATM;
import services.customExeptions.IncorrectPinException;
import services.customExeptions.NegativeBalanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Slf4j
@Controller
@RequestMapping("/ATM")
public class ATMController {

    @Autowired
    private ATM atm;

    @GetMapping()
    public String makeStartPageGet() {
        return "StartPage";
    }

    @PostMapping()
    @Transactional
    public String makeStartPagePost(@ModelAttribute("num") String cardNumber,
                                    @ModelAttribute("pin") String pinCode) {
        log.info("Введённый номер карты и пин:" + cardNumber + "\t" + pinCode);

        try {
            atm.authentication(cardNumber, pinCode);
        } catch (DaoException | IncorrectPinException e) {
            log.info(e.toString());
            return "redirect:/ATM/answer?answertext=" + e.getMessage();
        }
        log.info("Пост запрос на аутентификацию проведен");
        return "redirect:/ATM/mainmenu?num=" + cardNumber + "&pin=" + pinCode;
    }

    @GetMapping("/answer")
    public String makeErrorPage(@ModelAttribute("answertext") String answerMessage) {
        return "AnswerPage";
    }

    @GetMapping("/mainmenu")
    public String makeMainMenuPage(@ModelAttribute("num") String cardNumber,
                                   @ModelAttribute("pin") String pinCode) {
        return "MainMenu";
    }

    @GetMapping("/balance")
    public String makeBalancePage(@RequestParam("num") String cardNumber,
                                  @RequestParam("pin") String pinCode,
                                  Model model) {
        try {
            model.addAttribute("message", atm.checkBalance(cardNumber, pinCode));
        } catch (DaoException | IncorrectPinException e) {
            log.info(e.toString());
            return "redirect:/ATM/answer?answertext=" + e.getMessage();
        }

        return "BalancePage";
    }

    @GetMapping("/transactions")
    public String makeTransactionsPage(@RequestParam("num") String cardNumber,
                                       @RequestParam("pin") String pinCode,
                                       Model model) {
        try {
            model.addAttribute("transactions", atm.searchTransactionsStory(cardNumber, pinCode));
        } catch (DaoException | IncorrectPinException e) {
            log.info(e.toString());
            return "redirect:/ATM/answer?answertext=" + e.getMessage();
        }

        return "TransactionsPage";
    }

    @GetMapping("/transfer")
    public String makeTransferPageGet(@ModelAttribute("num") String cardNumber,
                                      @ModelAttribute("pin") String pinCode) {
        return "TransferPage";
    }

    @Transactional
    @PostMapping("/transfer")
    public String makeTransferPagePost(@ModelAttribute("num") String cardNumber,
                                       @ModelAttribute("pin") String pinCode,
                                       @ModelAttribute("resipientNumber") String resipientCardNumber,
                                       @ModelAttribute("amount") String amount) {

        log.info("Введённый номер карты получателя: \t" + resipientCardNumber + ",\tСумма: " + amount);

        try {
            atm.transferPToP(cardNumber, pinCode, resipientCardNumber, amount);
        } catch (DaoException|IncorrectPinException|NegativeBalanceException e) {
            log.info(e.toString());
            return "redirect:/ATM/answer?answertext=" + e.getMessage();
        }
        log.info("Пост запрос на перевод проведен");
        return "redirect:/ATM/answer?answertext=Successfully!!!";
    }
}
