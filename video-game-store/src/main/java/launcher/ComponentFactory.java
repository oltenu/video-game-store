package launcher;

import controller.LoginController;
import database.DatabaseSingleton;
import javafx.stage.Stage;
import repository.game.VideoGameRepository;
import repository.game.VideoGameRepositoryMySQL;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.game.VideoGameService;
import service.game.VideoGameServiceImplementation;
import service.order.OrderService;
import service.order.OrderServiceImplementation;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImplementation;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final VideoGameService videoGameService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final VideoGameRepository videoGameRepository;
    private final OrderRepository orderRepository;
    private static ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests, stage);
        }

        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage) {
        Connection connection = DatabaseSingleton.getConnection(componentsForTests);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.videoGameRepository = new VideoGameRepositoryMySQL(connection);
        this.orderRepository = new OrderRepositoryMySQL(connection);

        this.videoGameService = new VideoGameServiceImplementation(videoGameRepository);
        this.authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);
        this.orderService = new OrderServiceImplementation(userRepository, videoGameRepository, orderRepository);

        this.loginView = new LoginView(stage);
        this.loginController = new LoginController(loginView, authenticationService);
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public VideoGameService getVideoGameService() {
        return videoGameService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public VideoGameRepository getVideoGameRepository() {
        return videoGameRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }
}