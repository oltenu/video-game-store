package service.user;

import model.User;
import model.validator.Notification;

public interface AuthenticationService {
    Notification<Boolean> register(String username, String password);

    Notification<User> login(String username, String password);

    boolean logout(User activeUser);
    String getUserSaltByUsername(String username);
    String generateSalt();
    String getUserSaltById(Long id);
    String hashPassword(String password);
}
