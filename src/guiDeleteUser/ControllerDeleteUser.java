package guiDeleteUser;

import java.util.List;

import database.Database;
import applicationMain.FoundationsMain;
import javafx.collections.FXCollections;

/**
 * ControllerDeleteUser
 * --------------------
 * This controller handles all the logic for the Delete User page:
 *  - Populating the dropdown list of users from the database
 *  - Tracking which user is selected
 *  - Performing the actual delete operation with safeguards
 *  - Handling navigation (Return, Logout, Quit)
 *
 * The View handles layout/UI, while this class responds to user actions.
 */
public class ControllerDeleteUser {

    // Shared database instance provided by FoundationsMain
    protected static Database theDatabase = FoundationsMain.database;

    // Tracks which username is currently selected in the dropdown
    protected static String selectedUser = "<Select a User>";

    /**
     * Initialize this controller when the page is loaded.
     * Populates the dropdown with the latest user list and refreshes the view.
     */
    protected static void start() {
        refreshUsernames();
        ViewDeleteUser.repaintTheWindow();
    }

    /**
     * Refresh the dropdown list of users by pulling from the database.
     * Always includes "<Select a User>" as the default option.
     */
    protected static void refreshUsernames() {
        List<String> listFromDb = theDatabase.getUserList();
        if (listFromDb == null || listFromDb.isEmpty()) {
            // Fallback in case the DB call fails
            listFromDb = java.util.Arrays.asList("<Select a User>");
        }
        ViewDeleteUser.combo_Usernames.setItems(
            FXCollections.observableArrayList(listFromDb)
        );
        ViewDeleteUser.combo_Usernames.getSelectionModel().selectFirst();
        selectedUser = "<Select a User>";
    }

    /**
     * Called whenever the user selects a different name in the dropdown.
     * Updates our internal "selectedUser" state and requests a repaint.
     */
    protected static void setSelectedUser(String name) {
        selectedUser = (name == null ? "<Select a User>" : name);
        ViewDeleteUser.repaintTheWindow();
    }

    /**
     * Perform the actual delete operation after the Delete button is pressed.
     * Several safeguards are included:
     *  - Must have selected a user
     *  - Cannot delete the currently logged-in user
     *  - Must type the username exactly into the confirmation field
     *  - User must actually exist in the database
     */
    protected static void performDelete() {
        if (selectedUser == null || selectedUser.equals("<Select a User>")) {
            ViewDeleteUser.setStatus("Please select a user first.");
            return;
        }

        // Prevent self-deletion (safety guard)
        String current = theDatabase.getCurrentUsername();
        if (current != null && current.equalsIgnoreCase(selectedUser)) {
            ViewDeleteUser.setStatus("You cannot delete the currently logged-in account.");
            return;
        }

        // Confirmation text must exactly match the username
        String typed = ViewDeleteUser.field_Confirm.getText();
        if (!selectedUser.equals(typed)) {
            ViewDeleteUser.setStatus("Type the username exactly to confirm deletion.");
            return;
        }

        // Double-check that the user still exists in the DB
        boolean exists = theDatabase.doesUserExist(selectedUser);
        if (!exists) {
            ViewDeleteUser.setStatus("No such user in database.");
            return;
        }

        // Attempt the delete
        boolean ok = false;
        try {
            ok = theDatabase.deleteUserAccount(selectedUser);
        } catch (Exception ex) {
            ok = false; // If DB error, report failure
        }

        if (ok) {
            // Success: refresh list and clear confirmation field
            ViewDeleteUser.setStatus("User '" + selectedUser + "' deleted.");
            refreshUsernames();
            ViewDeleteUser.field_Confirm.setText("");
            ViewDeleteUser.repaintTheWindow();
        } else {
            ViewDeleteUser.setStatus("Deletion failed. (User may not exist or database error.)");
        }
    }

    /**
     * Navigate back to Admin Home page, passing both the stage and the
     * invoking user so Admin Home knows who is logged in.
     */
    protected static void performReturn() {
        guiAdminHome.ViewAdminHome.displayAdminHome(
            ViewDeleteUser.theStage,
            ViewDeleteUser.invokingUser
        );
    }

    /**
     * Log out of the system: navigates back to the login page.
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewDeleteUser.theStage);
    }

    /**
     * Exit the application entirely.
     */
    protected static void performQuit() {
        System.exit(0);
    }
}
