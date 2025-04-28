package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.entities.SupportMessage;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.repositories.FavoriteRepository;
import kz.seisen.sellflowv4.repositories.SupportMessageRepository;
import kz.seisen.sellflowv4.repositories.UserRepository;
import kz.seisen.sellflowv4.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private FavoriteRepository favoriteRepo;
    @Autowired
    private SupportMessageRepository supportMessageRepo;

    public List<User> listAllUsers() {
        return userRepo.findAll();
    }

    public void updateRole(Long userId, String newRole) {
        User u = userRepo.findById(userId).orElseThrow();
        u.setRole(newRole);
        userRepo.save(u);
    }

    public void setEnabled(Long userId, boolean enabled) {
        User u = userRepo.findById(userId).orElseThrow();
        u.setEnabled(enabled);
        userRepo.save(u);
    }

    public List<Product> listAllProducts() {
        return productRepo.findAll();
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Optional<Product> opt = productRepo.findById(productId);
        if (opt.isPresent()) {
            Product p = opt.get();
            // 1) remove favorites first
            favoriteRepo.deleteByProduct(p);
            // 2) now delete the product (images cascade/orphanRemoved)
            productRepo.delete(p);
        }
    }


    public List<SupportMessage> listAllSupportMessages() {
        return supportMessageRepo.findAll();
    }
}
