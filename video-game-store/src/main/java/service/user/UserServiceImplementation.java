package service.user;

import model.Role;
import model.User;
import model.validator.Notification;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.*;

public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final RightsRolesRepository rightsRolesRepository;

    public UserServiceImplementation(UserRepository userRepository, AuthenticationService authenticationService, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> user.setRoles(rightsRolesRepository.findRolesForUser(user.getId())));

        return users;
    }

    public List<User> findByRole(String role) {
        List<Long> usersId = switch (role) {
            case ADMINISTRATOR -> rightsRolesRepository.findUsersWithRole(1L);
            case EMPLOYEE -> rightsRolesRepository.findUsersWithRole(2L);
            default -> rightsRolesRepository.findUsersWithRole(3L);
        };

        List<User> users = new ArrayList<>();
        usersId.forEach(id -> users.add(userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("User with id: %d not found".formatted(id)))));

        return users;
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

    @Override
    public Role findRoleByTitle(String role) {
        return rightsRolesRepository.findRoleByTitle(role);
    }
}
