package repository.order;

import model.JoinedOrder;
import model.Order;
import model.builder.JointOrderBuilder;
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
    public List<JoinedOrder> findAllCustomerOrders(Long customerId) {
        List<JoinedOrder> orders = new ArrayList<>();
        String query = """
                SELECT
                    `Order`.id as order_id,
                    video_game.name as game_name,
                    UserCustomer.username as customer_username,
                    UserEmployee.username as employee_username,
                    `Order`.amount,
                    `Order`.total_price
                FROM
                    `Order`
                JOIN
                    `video_game` ON `Order`.game_id = video_game.id
                JOIN
                    `User` as UserCustomer ON `Order`.customer_id = UserCustomer.id
                JOIN
                    `User` as UserEmployee ON `Order`.employee_id = UserEmployee.id
                WHERE customer_id = ?""";

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
    public List<JoinedOrder> findAllEmployeeSales(Long employeeId) {
        List<JoinedOrder> orders = new ArrayList<>();
        String query = """
                SELECT
                    `Order`.id as order_id,
                    video_game.name as game_name,
                    UserCustomer.username as customer_username,
                    UserEmployee.username as employee_username,
                    `Order`.amount,
                    `Order`.total_price
                FROM
                    `Order`
                JOIN
                    `video_game` ON `Order`.game_id = video_game.id
                JOIN
                    `User` as UserCustomer ON `Order`.customer_id = UserCustomer.id
                JOIN
                    `User` as UserEmployee ON `Order`.employee_id = UserEmployee.id
                WHERE employee_id = ?""";

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

    private List<JoinedOrder> extractOrders(ResultSet resultSet) throws SQLException {
        List<JoinedOrder> joinedOrders = new ArrayList<>();

        while (resultSet.next()) {
            JoinedOrder joinedOrder = new JointOrderBuilder()
                    .setId(resultSet.getLong("order_id"))
                    .setGameName(resultSet.getString("game_name"))
                    .setCustomerUsername(resultSet.getString("customer_username"))
                    .setEmployeeUsername(resultSet.getString("employee_username"))
                    .setAmount(resultSet.getInt("amount"))
                    .setTotalPrice(resultSet.getDouble("total_price"))
                    .build();


            joinedOrders.add(joinedOrder);
        }

        return joinedOrders;
    }
}
