package helper;

import javafx.scene.control.TextField;

public class Validation {
    /**
     * Check if a TextField is Empty or not
     *
     * @param field TextField.
     * @return boolean
     * @throws NullPointerException Jika field = null
     */
    public static boolean textIsEmpty(TextField field) {
        try {
            if (field.getText().trim().isEmpty()) {
                field.getStyleClass().add("text-input-error");
                return true;
            }
            field.getStyleClass().remove("text-input-error");
            return false;
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return false;
    }
}
