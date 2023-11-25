package model.validator;

import model.User;
import model.VideoGame;

import java.util.ArrayList;
import java.util.List;

public class OrderValidator {
    private final User user;
    private final VideoGame videoGame;
    private final Integer amount;
    private final List<String> errors;

    public OrderValidator(User user, VideoGame videoGame, Integer amount) {
        this.user = user;
        this.videoGame = videoGame;
        this.errors = new ArrayList<>();
        this.amount = amount;
    }

    public boolean validate() {
        validateCustomerHasEnoughMoney();
        validateAmount();

        return errors.isEmpty();
    }

    private void validateCustomerHasEnoughMoney() {
        if (user.getMoney() < videoGame.getPrice() * amount) {
            errors.add("Customer does not have enough money!");
        }
    }

    private void validateAmount() {
        if (amount > videoGame.getAmount()) {
            errors.add("There are not enough copies of this game!");
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        return String.join("\n", errors);
    }
}
