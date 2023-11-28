package launcher;

import controller.AdminController;
import controller.CustomerController;
import controller.EmployeeController;
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
import service.user.UserService;
import service.user.UserServiceImplementation;
import view.*;

import java.sql.Connection;

public class ComponentFactory {
    private final Window window;
    private final LoginScene loginScene;
    private final CustomerScene customerScene;
    private final EmployeeScene employeeScene;
    private final AdminScene adminScene;

    private final LoginController loginController;
    private final CustomerController customerController;
    private final EmployeeController employeeController;
    private final AdminController adminController;


    private final AuthenticationService authenticationService;
    private final VideoGameService videoGameService;
    private final OrderService orderService;
    private final UserService userService;

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

    private ComponentFactory(Boolean componentsForTests, Stage stage) {
        this.loginScene = new LoginScene();
        this.customerScene = new CustomerScene();
        this.employeeScene = new EmployeeScene();
        this.adminScene = new AdminScene();
        this.window = new Window(stage, loginScene, customerScene, employeeScene, adminScene);

        Connection connection = DatabaseSingleton.getConnection(componentsForTests);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.videoGameRepository = new VideoGameRepositoryMySQL(connection);
        this.orderRepository = new OrderRepositoryMySQL(connection);

        this.videoGameService = new VideoGameServiceImplementation(videoGameRepository);
        this.authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);
        this.orderService = new OrderServiceImplementation(userRepository, videoGameRepository, orderRepository);
        this.userService = new UserServiceImplementation(userRepository, authenticationService);

        this.loginController = new LoginController(loginScene, authenticationService, window);
        this.customerController = new CustomerController(window, customerScene, authenticationService,
                videoGameService, orderService);
        this.employeeController = new EmployeeController(window, employeeScene, authenticationService, videoGameService,
                orderService);
        this.adminController = new AdminController(window, adminScene, videoGameService, orderService, userService,
                authenticationService);
    }
}