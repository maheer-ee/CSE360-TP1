package guiDeleteUser;

import entityClasses.User;
import applicationMain.FoundationsMain;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * ViewDeleteUser
 * --------------
 * This is the View component for the Delete User feature.
 *
 * Responsibilities:
 *  - Build and manage the JavaFX UI for the Delete User page.
 *  - Provide static references to UI elements that the Controller manipulates.
 *  - Maintain references to the current Stage and invoking User
 *    (so navigation can return to Admin Home properly).
 *
 * Design Notes:
 *  - Uses static members for simplicity, consistent with the project’s other views.
 *  - Separates layout/setup logic into `setupStaticUI()`.
 *  - All Controller interactions are via listeners and event handlers wired here.
 */
public class ViewDeleteUser {

    // The Stage is shared across the entire application
    protected static Stage theStage;

    // Root Pane and Scene are rebuilt each time we display this page
    protected static Pane theRootPane;
    protected static Scene theScene;

    // Use same dimensions as other pages (from FoundationsMain)
    protected static double width = FoundationsMain.WINDOW_WIDTH;
    protected static double height = FoundationsMain.WINDOW_HEIGHT;

    // Keep track of the user who invoked this page (needed for Return navigation)
    public static User invokingUser;

    // UI components
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

    /**
     * Entry point to display this page.
     * - Saves the stage
     * - Stores the invoking user
     * - Builds the UI layout
     * - Calls into the Controller to initialize state
     */
    public static void displayDeleteUser(Stage s, User user) {
        theStage = s;
        invokingUser = user;

        // Create new root and scene fresh each time
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        setupStaticUI();
        ControllerDeleteUser.start();

        theStage.setTitle("CSE 360 Foundation Code: Delete User");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Build the static UI components and wire up event handlers.
     * This method is called each time we enter the page.
     */
    protected static void setupStaticUI() {
        theRootPane.getChildren().clear();

        // Title
        label_Title.setFont(Font.font("Arial", 36));
        label_Title.setLayoutX(20); label_Title.setLayoutY(15);

        // Dropdown for usernames
        label_Select.setFont(Font.font("Arial", 18));
        label_Select.setLayoutX(20); label_Select.setLayoutY(85);
        setupCombo(combo_Usernames, 400, 20, 115);
        combo_Usernames.valueProperty().addListener((obs, o, n) -> {
            ControllerDeleteUser.setSelectedUser(n);
        });

        // Confirmation field
        label_Confirm.setFont(Font.font("Arial", 18));
        label_Confirm.setLayoutX(20); label_Confirm.setLayoutY(185);
        field_Confirm.setMinWidth(400);
        field_Confirm.setLayoutX(20); field_Confirm.setLayoutY(215);

        // Delete button
        button_Delete.setMinWidth(160);
        button_Delete.setLayoutX(440); button_Delete.setLayoutY(215);
        button_Delete.setOnAction(e -> ControllerDeleteUser.performDelete());

        // Navigation buttons
        button_Return.setMinWidth(140);
        button_Return.setLayoutX(20); button_Return.setLayoutY(545);
        button_Return.setOnAction(e -> ControllerDeleteUser.performReturn());

        button_Logout.setMinWidth(140);
        button_Logout.setLayoutX(180); button_Logout.setLayoutY(545);
        button_Logout.setOnAction(e -> ControllerDeleteUser.performLogout());

        button_Quit.setMinWidth(140);
        button_Quit.setLayoutX(340); button_Quit.setLayoutY(545);
        button_Quit.setOnAction(e -> ControllerDeleteUser.performQuit());

        // Status message area
        label_Status.setFont(Font.font("Arial", 16));
        label_Status.setLayoutX(20); label_Status.setLayoutY(480);
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

    /**
     * Called by the Controller to repaint/redraw as needed.
     * Currently does nothing (placeholder for future use).
     */
    protected static void repaintTheWindow() {
        // Reserved for future enhancements
    }

    /**
     * Set the status message label at the bottom of the page.
     */
    public static void setStatus(String msg) {
        label_Status.setText(msg == null ? "" : msg);
    }

    /**
     * Utility to consistently format ComboBox components.
     */
    private static void setupCombo(ComboBox<String> c, double w, double x, double y) {
        c.setStyle("-fx-font: 16 Arial;");
        c.setMinWidth(w);
        c.setLayoutX(x);
        c.setLayoutY(y);
        c.setVisibleRowCount(10);
    }
}

