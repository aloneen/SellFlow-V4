package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.entities.SupportMessage;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        List<User> users = adminService.listAllUsers();
        String currentUsername = authentication.getName();
        model.addAttribute("users", users);
        model.addAttribute("currentUsername", currentUsername);
        return "admin/users";    // new template
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String role) {
        adminService.updateRole(id, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/ban")
    public String banUnban(@PathVariable Long id,
                           @RequestParam boolean enabled) {
        adminService.setEnabled(id, enabled);
        return "redirect:/admin/users";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = adminService.listAllProducts();
        model.addAttribute("products", products);
        return "admin/products";    // new template
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/messages")
    public String listMessages(Model model) {
        List<SupportMessage> messages = adminService.listAllSupportMessages();
        model.addAttribute("messages", messages);
        return "admin/messages";
    }

}
