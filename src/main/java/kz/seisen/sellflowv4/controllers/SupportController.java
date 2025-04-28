package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.SupportMessage;
import kz.seisen.sellflowv4.services.SupportMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private SupportMessageService supportMessageService;

    @GetMapping("/help")    public String help()    { return "support/help_center"; }
    @GetMapping("/contact") public String contact() { return "support/contact_us"; }
    @GetMapping("/privacy") public String privacy() { return "support/privacy_policy"; }
    @GetMapping("/terms")   public String terms()   { return "support/terms_of_service"; }



    @PostMapping("/contact")
    public String submitContact(@RequestParam Map<String, String> params, RedirectAttributes ra) {
        String name = params.get("name");
        String email = params.get("email");
        String messageContent = params.get("message");

        SupportMessage message = new SupportMessage(name, email, messageContent, LocalDateTime.now());
        supportMessageService.save(message);

        ra.addFlashAttribute("message", "Thanks—we’ll be in touch!");
        return "redirect:/support/contact";
    }
}
