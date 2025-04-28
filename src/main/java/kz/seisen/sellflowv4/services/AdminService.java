package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired private UserRepository userRepo;

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
}
