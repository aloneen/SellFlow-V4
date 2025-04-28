package kz.seisen.sellflowv4.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/support")
public class SupportController {
    @GetMapping("/help")    public String help()    { return "support/help_center"; }
    @GetMapping("/contact") public String contact() { return "support/contact_us"; }
    @GetMapping("/privacy") public String privacy() { return "support/privacy_policy"; }
    @GetMapping("/terms")   public String terms()   { return "support/terms_of_service"; }
    @PostMapping("/contact")


    public String submitContact(@RequestParam Map<String,String> params, RedirectAttributes ra) {


        ra.addFlashAttribute("message","Thanks—we’ll be in touch!");
        return "redirect:/support/contact";
    }
}
