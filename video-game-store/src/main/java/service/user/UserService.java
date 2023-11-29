package service.user;

import model.Role;
import model.User;
import model.validator.Notification;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    Notification<Boolean> save(String userName, String password);

    void deleteById(Long id);

    boolean update(User user);

    void removeAll();

    List<User> findByRole(String role);

    Role findRoleByTitle(String role);
}
