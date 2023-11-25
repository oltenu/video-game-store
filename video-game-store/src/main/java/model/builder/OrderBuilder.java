package model.builder;

import model.Order;

public class OrderBuilder {
    private final Order order;

    public OrderBuilder() {
        order = new Order();
    }

    public OrderBuilder setId(Long id) {
        order.setId(id);

        return this;
    }

    public OrderBuilder setEmployeeId(Long id) {
        order.setEmployeeId(id);

        return this;
    }

    public OrderBuilder setCustomerId(Long id) {
        order.setCustomerId(id);

        return this;
    }

    public OrderBuilder setGameId(Long id) {
        order.setGameId(id);

        return this;
    }

    public OrderBuilder setAmount(Integer amount) {
        order.setAmount(amount);

        return this;
    }

    public OrderBuilder setTotalPrice(Double totalPrice) {
        order.setTotalPrice(totalPrice);

        return this;
    }

    public Order build() {
        return order;
    }
}
