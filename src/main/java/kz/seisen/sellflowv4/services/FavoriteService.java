package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.Favorite;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired private FavoriteRepository favoriteRepo;
    @Autowired private UserService userService;
    @Autowired private ProductService productService;

    public void addFavorite(String username, Long productId) {
        User user = userService.findByUsername(username);
        Product product = productService.getProductById(productId);
        if (user == null || product == null) return;
        // avoid duplicates
        if (favoriteRepo.findByUserAndProduct(user, product).isEmpty()) {
            favoriteRepo.save(new Favorite(user, product));
        }
    }

    public void removeFavorite(String username, Long productId) {
        User user = userService.findByUsername(username);
        Product product = productService.getProductById(productId);
        if (user == null || product == null) return;
        favoriteRepo.deleteByUserAndProduct(user, product);
    }

    public List<Favorite> listFavorites(String username) {
        User user = userService.findByUsername(username);
        return (user == null) ? List.of() : favoriteRepo.findByUser(user);
    }

    public boolean isFavorited(String username, Long productId) {
        User user = userService.findByUsername(username);
        Product product = productService.getProductById(productId);
        return user != null && product != null &&
                favoriteRepo.findByUserAndProduct(user, product).isPresent();
    }
}
