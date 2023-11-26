package repository.user;

import model.User;
import model.validator.Notification;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    boolean save(User user);

    void deleteById(Long id);

    boolean update(User user);

    void removeAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}