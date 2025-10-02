package guiDeleteUser;

import java.util.List;

import database.Database;
import applicationMain.FoundationsMain;
import javafx.collections.FXCollections;

/*******
 * <p> Title: ControllerDeleteUser Class. </p>
 * 
 * <p> Description: This controller manages the logic for the Delete User page. 
 * Responsibilities include: </p>
 * 
 * 
 * <p> The View manages layout/UI, while this controller responds to user actions. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00        2025-10-02 Initial version
 *  
 */

public class ControllerDeleteUser {
    
    /*-********************************************************************************************
    
    Attributes
    
    *********************************************************************************************/

    // Shared database instance provided by FoundationsMain
    protected static Database theDatabase = FoundationsMain.database;

    // Tracks which username is currently selected in the dropdown
    protected static String selectedUser = "<Select a User>";


    /*-********************************************************************************************
    
    Methods
    
    *********************************************************************************************/

    /**********
     * <p> Method: start() </p>
     * 
     * <p> Description: This method initializes the controller when the page is loaded.
     * It populates the dropdown with the latest user list and refreshes the view. </p>
     */
    protected static void start() {
        refreshUsernames();
        ViewDeleteUser.repaintTheWindow();
    }


    /**********
     * <p> Method: refreshUsernames() </p>
     * 
     * <p> Description: This method refreshes the dropdown list of users by pulling from
     * the database. It always includes "&lt;Select a User&gt;" as the default option. </p>
     */
    protected static void refreshUsernames() {
        List<String> listFromDb = theDatabase.getUserList();
        if (listFromDb == null || listFromDb.isEmpty()) {
            // Fallback in case the DB call fails
            listFromDb = java.util.Arrays.asList("<Select a User>");
        }
        ViewDeleteUser.combo_Usernames.setItems(
                FXCollections.observableArrayList(listFromDb));
        ViewDeleteUser.combo_Usernames.getSelectionModel().selectFirst();
        selectedUser = "<Select a User>";
    }


    /**********
     * <p> Method: setSelectedUser(String name) </p>
     * 
     * <p> Description: This method is called whenever the user selects a different
     * name in the dropdown. It updates the internal "selectedUser" state and requests
     * a repaint of the window. </p>
     * 
     * @param name The user selected in the dropdown
     */
    protected static void setSelectedUser(String name) {
        selectedUser = (name == null ? "<Select a User>" : name);
        ViewDeleteUser.repaintTheWindow();
    }


    /**********
     * <p> Method: performDelete() </p>
     * 
     * <p> Description: This method performs the actual delete operation after the
     * Delete button is pressed. Safeguards include: </p>
     * <ul>
     *   <li>User must be selected</li>
     *   <li>Cannot delete the currently logged-in user</li>
     *   <li>User must type the username exactly in the confirmation field</li>
     *   <li>User must exist in the database</li>
     * </ul>
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
            ViewDeleteUser.setStatus("User '" + selectedUser + "' deleted.");
            refreshUsernames();
            ViewDeleteUser.field_Confirm.setText("");
            ViewDeleteUser.repaintTheWindow();
        } else {
            ViewDeleteUser.setStatus("Deletion failed. (User may not exist or database error.)");
        }
    }


    /**********
     * <p> Method: performReturn() </p>
     * 
     * <p> Description: This method navigates back to the Admin Home page,
     * passing both the Stage and invoking user. </p>
     */
    protected static void performReturn() {
        guiAdminHome.ViewAdminHome.displayAdminHome(
            ViewDeleteUser.theStage,
            ViewDeleteUser.invokingUser);
    }


    /**********
     * <p> Method: performLogout() </p>
     * 
     * <p> Description: This method logs out of the system and navigates back to
     * the login page. </p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewDeleteUser.theStage);
    }


    /**********
     * <p> Method: performQuit() </p>
     * 
     * <p> Description: This method exits the application entirely. </p>
     */
    protected static void performQuit() {
        System.exit(0);
    }
}
