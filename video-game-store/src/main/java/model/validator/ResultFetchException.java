package model.validator;

import java.util.List;
import java.util.stream.Collectors;

public class ResultFetchException extends RuntimeException {
    private final List<String> errors;

    public ResultFetchException(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return errors.stream().map(String::toString).collect(Collectors.joining(", ")) + super.toString();
    }
}
