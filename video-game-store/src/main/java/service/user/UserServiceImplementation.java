package service.user;

import model.User;
import model.validator.Notification;
import repository.user.UserRepository;

import java.util.List;

public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserServiceImplementation(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("User with %d id not found!".formatted(id)));
    }

    @Override
    public Notification<Boolean> save(String userName, String password) {
        return authenticationService.register(userName, password);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public void removeAll() {
        userRepository.removeAll();
    }
}
