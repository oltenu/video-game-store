package service.order;

import model.JoinedOrder;
import model.Order;
import model.validator.Notification;

import java.util.List;

public interface OrderService {
    List<Order> findAll();

    Order findById(Long id);

    boolean save(Order order);

    void deleteById(Long id);

    boolean update(Order order);

    void removeAll();

    Notification<Boolean> buyGame(Long userId, Long gameId, Integer amount);

    List<JoinedOrder> findAllCustomerOrders(Long customerId);

    List<JoinedOrder> findAllEmployeeSales(Long employeeId);
}
