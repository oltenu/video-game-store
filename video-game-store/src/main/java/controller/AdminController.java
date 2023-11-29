package controller;

import service.game.VideoGameService;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminScene;
import view.Window;

public class AdminController extends CustomerController {
    private final Window window;
    private final AdminScene adminScene;
    private final VideoGameService videoGameService;
    private final OrderService orderService;
    private final UserService userService;

    public AdminController(Window window, AdminScene adminScene, VideoGameService videoGameService,
                           OrderService orderService, UserService userService, AuthenticationService authenticationService) {
        super(window, adminScene, authenticationService, videoGameService, orderService);

        this.window = window;
        this.adminScene = adminScene;
        this.videoGameService = videoGameService;
        this.orderService = orderService;
        this.userService = userService;
    }
}
