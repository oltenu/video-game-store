package repository.order;

import model.Order;
import model.builder.OrderBuilder;
import repository.AbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySQL extends AbstractRepository<Order> implements OrderRepository {
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection) {
        super(connection, Order.class);
        this.connection = connection;
    }

    @Override
    public List<Order> findAllCustomerOrders(Long customerId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE customer_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            orders = extractOrders(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> findAllEmployeeSales(Long employeeId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order` WHERE employee_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            orders = extractOrders(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private List<Order> extractOrders(ResultSet resultSet) throws SQLException {
        List<Order> orders = new ArrayList<>();

        while (resultSet.next()) {
            Order order = new OrderBuilder()
                    .setId(resultSet.getLong("id"))
                    .setCustomerId(resultSet.getLong("customer_id"))
                    .setEmployeeId(resultSet.getLong("employee_id"))
                    .setGameId(resultSet.getLong("game_id"))
                    .setAmount(resultSet.getInt("amount"))
                    .setTotalPrice(resultSet.getDouble("total_price"))
                    .build();


            orders.add(order);
        }

        return orders;
    }
}
