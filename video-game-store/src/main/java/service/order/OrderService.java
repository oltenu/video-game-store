package service.order;

import model.validator.Notification;

public interface OrderService {
    Notification<Boolean> buyGame(Long userId, Long gameId, Integer amount);
}
