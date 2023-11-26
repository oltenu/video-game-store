package view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class EmployeeScene extends CustomerScene {
    protected Menu employeeMenu;
    protected MenuItem gamesItem;
    protected MenuItem salesReport;

    public EmployeeScene(){
        initializeMenu();
    }

    private void initializeMenu(){
        employeeMenu = new Menu("Employee");
        gamesItem = new MenuItem("Games...");
        salesReport = new MenuItem("Sales...");

        employeeMenu.getItems().addAll(gamesItem, salesReport);
        menuBar.getMenus().add(employeeMenu);
    }


}
