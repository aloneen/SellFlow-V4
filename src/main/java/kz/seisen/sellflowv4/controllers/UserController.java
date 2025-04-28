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

import java.security.Principal;
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

        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        List<Product> productsByUser = productService.findByAuthor(username);
        model.addAttribute("products", productsByUser);

        return "profile";
    }

    @GetMapping("/users/{username}")
    public String viewUserProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/?error=userNotFound";
        }
        // fetch that user’s products
        List<Product> products = productService.findByAuthor(username);
        model.addAttribute("user", user);
        model.addAttribute("products", products);
        return "user_profile";
    }


    @GetMapping("/profile/edit")
    public String showEditForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("userForm", user);
        return "edit_profile";
    }

    @PostMapping("/profile/edit")
    public String processEditForm(
            @ModelAttribute("userForm") User userForm,
            @RequestParam(required = false) String newPassword,
            Principal principal
    ) {
        // always update *this* user
        String username = principal.getName();
        userService.updateProfile(
                username,
                userForm.getEmail(),
                userForm.getNumber(),
                newPassword
        );
        return "redirect:/profile";
    }




    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
