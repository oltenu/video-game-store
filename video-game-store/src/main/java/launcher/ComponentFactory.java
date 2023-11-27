package launcher;

import controller.LoginController;
import database.DatabaseSingleton;
import javafx.stage.Stage;
import model.User;
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
import view.*;

import java.sql.Connection;

public class ComponentFactory {
    private final Window window;
    private final LoginScene loginScene;
    private final CustomerScene customerScene;
    private final EmployeeScene employeeScene;
    private final AdminScene adminScene;

    private final LoginController loginController;


    private final AuthenticationService authenticationService;
    private final VideoGameService videoGameService;
    private final OrderService orderService;

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final VideoGameRepository videoGameRepository;
    private final OrderRepository orderRepository;

    private User activeUser;

    private static ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests, stage);
        }

        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage) {
        activeUser = null;

        loginScene = new LoginScene();
        customerScene = new CustomerScene();
        employeeScene = new EmployeeScene();
        adminScene = new AdminScene();
        window = new Window(stage, loginScene, customerScene, employeeScene, adminScene);

        Connection connection = DatabaseSingleton.getConnection(componentsForTests);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.videoGameRepository = new VideoGameRepositoryMySQL(connection);
        this.orderRepository = new OrderRepositoryMySQL(connection);

        this.videoGameService = new VideoGameServiceImplementation(videoGameRepository);
        this.authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);
        this.orderService = new OrderServiceImplementation(userRepository, videoGameRepository, orderRepository);

        this.loginController = new LoginController(loginScene, authenticationService, window, activeUser);
    }

    public LoginScene getLoginView() {
        return loginScene;
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