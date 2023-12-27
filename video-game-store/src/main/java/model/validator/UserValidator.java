package model.validator;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final int MIN_PASSWORD_LENGTH = 8;
    private final List<String> errors;
    private final User user;

    public UserValidator(User user) {
        this.errors = new ArrayList<>();
        this.user = user;
    }

    public boolean validate(boolean update) {
        validateUsername();
        if(!update){
            validatePassword();
        }

        return errors.isEmpty();
    }

    private void validateUsername() {
        if (!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(user.getUsername()).matches()) {
            errors.add("Email is not valid!");
        }
    }

    private void validatePassword() {
        if (user.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.add(String.format("Password must be at least %d characters long!", MIN_PASSWORD_LENGTH));
        }

        if (!containsSpecialCharacter()) {
            errors.add("Password must contain at least one special character.");
        }

        if (!containsDigit()) {
            errors.add("Password must contain at least one digit!");
        }
    }

    private boolean containsSpecialCharacter() {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return false;
        }
        Pattern specialCharactersPattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher specialCharactersMatcher = specialCharactersPattern.matcher(user.getPassword());

        return specialCharactersMatcher.find();
    }

    private boolean containsDigit() {
        return Pattern.compile(".*[0-9].*").matcher(user.getPassword()).find();
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        return String.join("\n", errors);
    }
}
