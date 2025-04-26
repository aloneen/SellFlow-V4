package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
@RequestMapping("/favorites")
public class FavoritesController {

    @Autowired private FavoriteService favoriteService;

    @PostMapping("/add")
    public String addFav(@AuthenticationPrincipal UserDetails user,
                         @RequestParam Long productId) {
        favoriteService.addFavorite(user.getUsername(), productId);
        return "redirect:/" + productId;
    }

    @PostMapping("/remove")
    public String removeFav(@AuthenticationPrincipal UserDetails user,
                            @RequestParam Long productId) {
        favoriteService.removeFavorite(user.getUsername(), productId);
        return "redirect:/" + productId;
    }

    @GetMapping
    public String viewFavs(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("favorites", favoriteService.listFavorites(user.getUsername()));
        return "favorites";  // new Thymeleaf template
    }
}
