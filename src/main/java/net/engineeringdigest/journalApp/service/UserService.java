package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public List<User> getAll() {
            return userRepository.findAll();
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
