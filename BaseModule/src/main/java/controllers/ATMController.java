package controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import services.customExeptions.CardNotFoundException;
import services.ATM;
import services.customExeptions.IncorrectPinException;
import services.customExeptions.NegativeBalanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.customExeptions.ViolationUniquenessException;
import services.entity.User;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/ATM")
public class ATMController {

    private final ATM atm;

    @GetMapping()
    public String makeStartPageGet() {
        return "StartPage";
    }

    @PostMapping()
    public String makeStartPagePost(@ModelAttribute("num") String cardNumber,
                                    @ModelAttribute("pin") String pinCode) {
        try {
            atm.authentication(cardNumber, pinCode);
        } catch (CardNotFoundException | IncorrectPinException e) {
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
        } catch (CardNotFoundException | IncorrectPinException e) {
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
        } catch (CardNotFoundException | IncorrectPinException e) {
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

    @PostMapping("/transfer")
    public String makeTransferPagePost(@ModelAttribute("num") String cardNumber,
                                       @ModelAttribute("pin") String pinCode,
                                       @ModelAttribute("resipientNumber") String resipientCardNumber,
                                       @ModelAttribute("amount") String amount) {

        log.info("Введённый номер карты получателя: \t" + resipientCardNumber + ",\tСумма: " + amount);

        try {
            atm.transferPToP(cardNumber, pinCode, resipientCardNumber, amount);
        } catch (CardNotFoundException |IncorrectPinException|NegativeBalanceException e) {
            log.info(e.toString());
            return "redirect:/ATM/answer?answertext=" + e.getMessage();
        }
        log.info("Пост запрос на перевод проведен");
        return "redirect:/ATM/answer?answertext=Successfull!!!";
    }

    @GetMapping("/open")
    public String makeOpenCardPageGet(@ModelAttribute("user") User user) {
        return "OpenCardPage";
    }

    @PostMapping("/open")
    public String makeOpenCardPagePost(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "OpenCardPage";
        }

        try {
            atm.createNewCard(user);
        } catch (ViolationUniquenessException e) {
            log.info(e.toString());
            return "redirect:/ATM/answer?answertext=" + e.getMessage();
        }
        return "redirect:/ATM/answer?answertext=Successfull!!!";
    }
}
