package repository.order;

import model.Order;
import repository.AbstractRepository;

import java.sql.Connection;

public class OrderRepositoryMySQL extends AbstractRepository<Order> implements OrderRepository {
    public OrderRepositoryMySQL(Connection connection) {
        super(connection, Order.class);
    }
}
