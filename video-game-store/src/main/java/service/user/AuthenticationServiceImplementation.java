package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;

import static database.Constants.Roles.CUSTOMER;

public class AuthenticationServiceImplementation implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private static final int SALT_LENGTH = 16;

    public AuthenticationServiceImplementation(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password) {

        Role customerRole = rightsRolesRepository.findRoleByTitle(CUSTOMER);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setMoney(100.0)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        Notification<Boolean> userRegisterNotification = new Notification<>();
        userRegisterNotification.setResult(Boolean.TRUE);

        if (!validateEmailUniqueness(username)) {
            userRegisterNotification.addError("Email is already taken!");
            userRegisterNotification.setResult(Boolean.FALSE);
        }

        if (!userValid || !userRegisterNotification.getResult()) {
            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult(Boolean.FALSE);
        } else {
            String salt = generateSalt();
            user.setPassword(hashPassword(password + salt));

            userRegisterNotification.setResult(userRepository.save(user, salt));
        }

        return userRegisterNotification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            String salt = getUserSaltByUsername(username);
            return userRepository.findByUsernameAndPassword(username, hashPassword(password + salt));
        } else {
            Notification<User> notification = new Notification<>();
            notification.addError("Invalid username or password");
            return notification;
        }

    }

    @Override
    public boolean logout(User activeUser) {
        activeUser = null;

        return true;
    }

    private boolean validateEmailUniqueness(String email) {
        final boolean response = userRepository.existsByUsername(email);

        return !response;
    }

    @Override
    public String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    @Override
    public String getUserSaltByUsername(String username) {
        return userRepository.getUserSalt(userRepository.getUserId(username));
    }

    @Override
    public String getUserSaltById(Long id) {
        return userRepository.getUserSalt(id);
    }

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
