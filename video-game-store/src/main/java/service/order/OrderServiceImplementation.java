package service.order;

import model.Order;
import model.User;
import model.VideoGame;
import model.builder.OrderBuilder;
import model.validator.Notification;
import model.validator.OrderValidator;
import repository.game.VideoGameRepository;
import repository.order.OrderRepository;
import repository.user.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static database.Constants.Roles.EMPLOYEE;

public class OrderServiceImplementation implements OrderService {
    private final UserRepository userRepository;
    private final VideoGameRepository videoGameRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImplementation(UserRepository userRepository, VideoGameRepository videoGameRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.videoGameRepository = videoGameRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Notification<Boolean> buyGame(Long customerId, Long gameId, Integer amount) {
        Notification<Boolean> buyGameNotification = new Notification<>();

        User user = userRepository.findById(customerId).orElse(null);
        VideoGame game = videoGameRepository.findById(gameId).orElse(null);

        List<User> users = userRepository.findAll();
        Collections.shuffle(users);
        User employee = users.stream()
                .filter(u -> u.getRoles()
                        .stream()
                        .anyMatch(role -> EMPLOYEE.equals(role.getRole()))).findFirst().orElse(null);
        buyGameNotification.setResult(Boolean.TRUE);

        if (user == null) {
            buyGameNotification.addError("Customer with %d id not found!".formatted(customerId));
            buyGameNotification.setResult(Boolean.FALSE);
        }

        if (game == null) {
            buyGameNotification.addError("Video game with %d id not found!".formatted(gameId));
            buyGameNotification.setResult(Boolean.FALSE);
        }

        if (employee == null) {
            buyGameNotification.addError("No employee available!");
            buyGameNotification.setResult(Boolean.FALSE);
        }


        OrderValidator orderValidator = new OrderValidator(user, game, amount);

        if (!orderValidator.validate() || !buyGameNotification.getResult()) {
            orderValidator.getErrors().forEach(buyGameNotification::addError);
            buyGameNotification.setResult(Boolean.FALSE);
        } else {
            Order order = new OrderBuilder()
                    .setCustomerId(customerId)
                    .setEmployeeId(Objects.requireNonNull(employee).getId())
                    .setGameId(gameId)
                    .setAmount(amount)
                    .setTotalPrice(amount * Objects.requireNonNull(game).getPrice())
                    .build();

            buyGameNotification.setResult(orderRepository.save(order));
        }

        return buyGameNotification;
    }
}
