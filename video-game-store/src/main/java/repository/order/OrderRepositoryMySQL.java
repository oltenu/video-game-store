package repository.order;

import model.JointOrder;
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
    public List<JointOrder> findAllCustomerOrders(Long customerId) {
        List<JointOrder> orders = new ArrayList<>();
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
    public List<JointOrder> findAllEmployeeSales(Long employeeId) {
        List<JointOrder> orders = new ArrayList<>();
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

    private List<JointOrder> extractOrders(ResultSet resultSet) throws SQLException {
        List<JointOrder> jointOrders = new ArrayList<>();

        while (resultSet.next()) {
            JointOrder jointOrder = new JointOrderBuilder()
                    .setId(resultSet.getLong("order_id"))
                    .setGameName(resultSet.getString("game_name"))
                    .setCustomerUsername(resultSet.getString("customer_username"))
                    .setEmployeeUsername(resultSet.getString("employee_username"))
                    .setAmount(resultSet.getInt("amount"))
                    .setTotalPrice(resultSet.getDouble("total_price"))
                    .build();


            jointOrders.add(jointOrder);
        }

        return jointOrders;
    }
}
