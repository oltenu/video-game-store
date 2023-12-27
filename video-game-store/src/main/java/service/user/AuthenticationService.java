package service.user;

import model.User;
import model.validator.Notification;

public interface AuthenticationService {
    Notification<Boolean> register(String username, String password);

    Notification<User> login(String username, String password);

    Notification<Boolean> register(String username, String password, String money, String role);

    String getUserSaltByUsername(String username);

    String generateSalt();

    String getUserSaltById(Long id);

    String hashPassword(String password);

    boolean validateEmailUniqueness(String email);
}
