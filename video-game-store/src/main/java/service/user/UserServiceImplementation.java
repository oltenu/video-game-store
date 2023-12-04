package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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
    public Notification<Boolean> update(Long id, String username, String password, String money, String role) {
        Notification<Boolean> resultNotification = new Notification<>();
        Role userRole = rightsRolesRepository.findRoleByTitle(role);
        double moneyAmount = 0;

        if (money.isEmpty() || !Pattern.matches("^\\d*\\.?\\d+$", money)) {
            resultNotification.addError("Invalid money amount!");
        } else {
            moneyAmount = Double.parseDouble(money);
        }

        User user = new UserBuilder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setMoney(moneyAmount)
                .setRoles(Collections.singletonList(userRole))
                .build();

        User dbUser = findById(user.getId());
        if (user.getUsername().isEmpty()) {
            user.setUsername(dbUser.getUsername());
        }

        boolean update = false;
        if (user.getPassword().isEmpty()) {
            user.setPassword(dbUser.getPassword());
            update = true;
        }

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate(update);

        if (authenticationService.validateEmailUniqueness(username) && !username.equals(dbUser.getUsername())) {
            resultNotification.addError("Email is already taken!");
            resultNotification.setResult(Boolean.FALSE);
        }

        if (!userValid || resultNotification.hasErrors()) {
            userValidator.getErrors().forEach(resultNotification::addError);

            resultNotification.setResult(Boolean.FALSE);
        } else {
            if (!update) {
                String salt = authenticationService.getUserSaltById(user.getId());
                user.setPassword(authenticationService.hashPassword(user.getPassword() + salt));
            }

            userRepository.update(user);
        }

        return resultNotification;
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
