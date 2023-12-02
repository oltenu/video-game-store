package repository.order;

import model.JointOrder;
import model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();

    Optional<Order> findById(Long id);

    boolean save(Order order);

    void deleteById(Long id);

    boolean update(Order order);

    void removeAll();

    List<JointOrder> findAllCustomerOrders(Long customerId);

    List<JointOrder> findAllEmployeeSales(Long employeeId);
}
