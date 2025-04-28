package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private AdminService adminService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = adminService.listAllUsers();
        model.addAttribute("users", users);
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

}
