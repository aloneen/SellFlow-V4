package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void updateProfile(
            String oldUsername,
            String newUsername,
            String newEmail,
            String newNumber,
            String newPassword
    ) {
        User user = userRepository.findByUsername(oldUsername);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // apply changes
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setNumber(newNumber);

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByNumber(String number) {
        return userRepository.findByNumber(number);
    }

}
