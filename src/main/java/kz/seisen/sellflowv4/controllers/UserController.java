package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.repositories.UserRepository;
import kz.seisen.sellflowv4.services.ProductService;
import kz.seisen.sellflowv4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public UserController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerNewUser(user);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        }

        return "redirect:/login?registerSuccess";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Optionally, load user details from the repository if you want more information
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        // You might also list products created by this user based on the "author" field
        List<Product> productsByUser = productService.findByAuthor(username);
        model.addAttribute("products", productsByUser);

        return "profile";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
