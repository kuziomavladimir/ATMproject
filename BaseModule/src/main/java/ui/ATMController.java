package ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/ATM")
public class ATMController {

    @Autowired
    private ScriptsController scriptsController;

    @GetMapping()
    public String findAndAutenticate(@ModelAttribute("number") String number, @ModelAttribute("pin") String pin) {
        return "StartPage";
    }

    @PostMapping()
    public String create(@ModelAttribute("number") String number, @ModelAttribute("pin") String pin) {

        log.info("Введённый номер карты и пин:" + number + "\t" + pin);

//        scriptsController.doCheckBalance(number, pin);
        scriptsController.showTransactions(number, pin);


        log.info("Пост запрос проведен");
        return "redirect:/ATM";
    }

    @GetMapping("/transfer")
    public String doTransferGet(@ModelAttribute("senderNumber") String senderNumber,
                                @ModelAttribute("pin") String pin,
                                @ModelAttribute("resipientNumber") String resipientNumber,
                                @ModelAttribute("amount") String amount) {
        return "TransferPage";
    }

    @PostMapping("/transfer")
    public String doTransferPost(@ModelAttribute("senderNumber") String senderNumber,
                                 @ModelAttribute("pin") String pin,
                                 @ModelAttribute("resipientNumber") String resipientNumber,
                                 @ModelAttribute("amount") String amount) {

        log.info("Введённый номер карты и пин:" + senderNumber + "\t" + pin + "\tполучатель: \t" + resipientNumber);

        scriptsController.doTransfer(senderNumber, pin, resipientNumber, amount);

        log.info("Пост запрос проведен");
        return "redirect:/ATM";
    }

}
