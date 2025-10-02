package guiUserUpdate;

import javafx.scene.paint.Color;
import validators.EmailAddressRecognizer;

public class Model {
	/*
     * This attribute stores the last error message for validation purposes.
     */
    public static String lastErrorMessage = "";


    /*****
     * <p> Method: validateEmailAddress(String emailAddress) </p>
     * 
     * <p> Description: This method validates the email address using FSM rules from the 
     * UserNameRecognizer class. It updates the View with error or success messages. </p>
     * 
     * @param emailAddress the input string to be validated
     * 
     * @return true if the email address is valid, false otherwise
     */
    protected static boolean validateEmailAddress(String emailAddress) {
        String error = EmailAddressRecognizer.checkEmailAddress(emailAddress);
        if (!error.isEmpty()) {
            lastErrorMessage = error;
            ViewUserUpdate.label_EmailAddressValidation.setTextFill(Color.RED);
            ViewUserUpdate.label_EmailAddressValidation.setText(error);
            return false;
        }
        ViewUserUpdate.label_EmailAddressValidation.setTextFill(Color.GREEN);
        ViewUserUpdate.label_EmailAddressValidation.setText("Valid email address.");
        return true;
    }
}
