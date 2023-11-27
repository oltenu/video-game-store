package view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class AdminScene extends CustomerScene{
    protected Menu adminMenu;
    protected MenuItem usersItem;
    protected MenuItem employeeReportItem;
    public AdminScene(){
        initializeMenuCustomer();
    }

    public void initializeMenuAdmin(){
        adminMenu = new Menu("Admin");

        usersItem = new MenuItem("Users...");
        employeeReportItem = new MenuItem("Employee Report...");

        adminMenu.getItems().addAll(usersItem, employeeReportItem);
        menuBar.getMenus().add(adminMenu);
    }
}
