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
    private static ComponentFactory instance;

    public static void getInstance(Boolean componentsForTests, Stage stage) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests, stage);
        }
    }

    private ComponentFactory(Boolean componentsForTests, Stage stage) {
        LoginScene loginScene = new LoginScene();
        CustomerScene customerScene = new CustomerScene();
        EmployeeScene employeeScene = new EmployeeScene();
        AdminScene adminScene = new AdminScene();
        Window window = new Window(stage, loginScene, customerScene, employeeScene, adminScene);

        Connection connection = DatabaseSingleton.getConnection(componentsForTests);
        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        VideoGameRepository videoGameRepository = new VideoGameRepositoryMySQL(connection);
        OrderRepository orderRepository = new OrderRepositoryMySQL(connection);

        VideoGameService videoGameService = new VideoGameServiceImplementation(videoGameRepository);
        AuthenticationService authenticationService = new AuthenticationServiceImplementation(userRepository, rightsRolesRepository);
        OrderService orderService = new OrderServiceImplementation(userRepository, videoGameRepository, orderRepository);
        UserService userService = new UserServiceImplementation(userRepository, authenticationService, rightsRolesRepository);

        new LoginController(loginScene, authenticationService, window);
        new CustomerController(window, customerScene,
                videoGameService, orderService, userService);
        new EmployeeController(window, employeeScene, videoGameService,
                orderService, userService);
        new AdminController(window, adminScene, videoGameService, orderService, userService,
                authenticationService);
    }
}