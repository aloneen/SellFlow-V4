package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.services.ProductService;
import kz.seisen.sellflowv4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final ProductService productService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(ProductService productService,
                          UserService userService,
                          UserDetailsService userDetailsService) {
        this.productService = productService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "Username already exists");
            return "register";
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Email already in use");
            return "register";
        }
        if (userService.findByNumber(user.getNumber()) != null) {
            model.addAttribute("numberError", "Phone number already in use");
            return "register";
        }

        userService.registerNewUser(user);
        return "redirect:/login?registerSuccess";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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
            Principal principal,
            Model model
    ) {
        String oldUsername = principal.getName();
        String newUsername = userForm.getUsername();

        // 1) username uniqueness
        if (!newUsername.equals(oldUsername)
                && userService.findByUsername(newUsername) != null) {
            model.addAttribute("usernameError", "Username already in use");
            return "edit_profile";
        }

        // 2) email uniqueness
        User byEmail = userService.findByEmail(userForm.getEmail());
        if (byEmail != null && !byEmail.getUsername().equals(oldUsername)) {
            model.addAttribute("emailError", "Email already in use");
            return "edit_profile";
        }

        // 3) phone uniqueness
        User byNumber = userService.findByNumber(userForm.getNumber());
        if (byNumber != null && !byNumber.getUsername().equals(oldUsername)) {
            model.addAttribute("numberError", "Phone number already in use");
            return "edit_profile";
        }

        // 4) persist updates
        userService.updateProfile(
                oldUsername,
                newUsername,
                userForm.getEmail(),
                userForm.getNumber(),
                newPassword
        );

        // 5) if username changed, re-authenticate under new name
        if (!newUsername.equals(oldUsername)) {
            UserDetails newDetails = userDetailsService.loadUserByUsername(newUsername);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    newDetails,
                    newDetails.getPassword(),
                    newDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        return "redirect:/profile";
    }
}
