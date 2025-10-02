package guiDeleteUser;

import entityClasses.User;
import applicationMain.FoundationsMain;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewDeleteUser Class. </p>
 * 
 * <p> Description: The Java/FX-based page for deleting a user account. </p>
 * 
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.01        2025-10-02 Font/Button style aligned with Admin Home
 *  
 */

public class ViewDeleteUser {
    
    /*-*******************************************************************************************
    
    Attributes
    
    *********************************************************************************************/

    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static Scene theScene;

    protected static double width = FoundationsMain.WINDOW_WIDTH;
    protected static double height = FoundationsMain.WINDOW_HEIGHT;

    public static User invokingUser;

    // GUI components
    protected static Label label_Title = new Label("Delete a User");
    protected static Line line_Separator1 = new Line(20, 65, width-20, 65);
    protected static Line line_Separator2 = new Line(20, 165, width-20, 165);
    protected static Line line_Separator3 = new Line(20, 525, width-20, 525);

    protected static Label label_Select = new Label("Select a user to delete:");
    public static ComboBox<String> combo_Usernames = new ComboBox<>();

    protected static Label label_Confirm = new Label("Type the username EXACTLY to confirm:");
    public static TextField field_Confirm = new TextField();
    protected static Button button_Delete = new Button("Delete User");

    protected static Button button_Return = new Button("Return");
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    protected static Label label_Status = new Label("");


    /*-*******************************************************************************************
    
    Constructors
    
    *********************************************************************************************/

    public static void displayDeleteUser(Stage s, User user) {
        theStage = s;
        invokingUser = user;

        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        setupStaticUI();
        ControllerDeleteUser.start();

        theStage.setTitle("CSE 360 Foundation Code: Delete User");
        theStage.setScene(theScene);
        theStage.show();
    }


    /*-*******************************************************************************************
    
    UI Setup
    
    *********************************************************************************************/

    protected static void setupStaticUI() {
        theRootPane.getChildren().clear();

        // Title (match Admin Home title style)
        setupLabelUI(label_Title, "Arial", 28, width, Pos.BASELINE_LEFT, 20, 15);

        // Dropdown for usernames
        setupLabelUI(label_Select, "Arial", 20, 400, Pos.BASELINE_LEFT, 20, 85);
        setupComboBoxUI(combo_Usernames, "Dialog", 16, 400, 20, 115);
        combo_Usernames.valueProperty().addListener((obs, o, n) -> {
            ControllerDeleteUser.setSelectedUser(n);
        });

        // Confirmation field
        setupLabelUI(label_Confirm, "Arial", 20, 400, Pos.BASELINE_LEFT, 20, 185);
        field_Confirm.setFont(Font.font("Arial", 16));
        field_Confirm.setMinWidth(400);
        field_Confirm.setLayoutX(20);
        field_Confirm.setLayoutY(215);

        // Delete button (match Admin Home buttons style)
        setupButtonUI(button_Delete, "Dialog", 16, 170, Pos.CENTER, 440, 215);
        button_Delete.setOnAction(e -> ControllerDeleteUser.performDelete());

        // Navigation buttons (same style as Admin Home bottom buttons)
        setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        button_Return.setOnAction(e -> ControllerDeleteUser.performReturn());

        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        button_Logout.setOnAction(e -> ControllerDeleteUser.performLogout());

        setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
        button_Quit.setOnAction(e -> ControllerDeleteUser.performQuit());

        // Status message
        setupLabelUI(label_Status, "Arial", 16, 400, Pos.BASELINE_LEFT, 20, 480);
        label_Status.setStyle("-fx-text-fill: #555;");

        // Add all widgets
        theRootPane.getChildren().addAll(
            label_Title,
            line_Separator1,
            label_Select, combo_Usernames,
            line_Separator2,
            label_Confirm, field_Confirm, button_Delete,
            label_Status,
            line_Separator3,
            button_Return, button_Logout, button_Quit
        );
    }


    /*-*******************************************************************************************
    
    Helper Methods
    
    *********************************************************************************************/

    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }

    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }

    private static void setupComboBoxUI(ComboBox<String> c, String ff, double f, double w, double x, double y){
        c.setStyle("-fx-font: " + f + " " + ff + ";");
        c.setMinWidth(w);
        c.setLayoutX(x);
        c.setLayoutY(y);
    }

    protected static void repaintTheWindow() {
        // Reserved for future enhancements
    }

    public static void setStatus(String msg) {
        label_Status.setText(msg == null ? "" : msg);
    }
}
