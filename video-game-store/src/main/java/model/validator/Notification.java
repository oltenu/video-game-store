package model.validator;

import java.util.ArrayList;
import java.util.List;

public class Notification<T> {
    private T result;
    private final List<String> errors;

    public Notification() {
        errors = new ArrayList<>();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        if (hasErrors()) {
            throw new ResultFetchException(errors);
        }
        return result;
    }

    public String getFormattedErrors() {
        return String.join("\n", errors);
    }
}
