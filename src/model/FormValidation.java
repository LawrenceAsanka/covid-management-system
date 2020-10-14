package model;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class FormValidation {
    public static boolean textFieldIsNull(JFXTextField t1, Label l1, String validationText){
        boolean b = true;
        String s=null;

        if (t1.getText().trim().isEmpty()) {
            b=false;
            s=validationText;
        }
        l1.setText(s);
        return b;
    }

    public static boolean nameValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([\\sa-zA-Z])*")) {
            b=false;
            s=validationText;
            t.requestFocus();
            t.deselect();
        }
        l.setText(s);
        return b;
    }

    public static boolean cityValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([\\sa-zA-Z0-9])*")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }

    public static boolean comboboxValidation(ComboBox t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getSelectionModel().isEmpty()) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }
    public static boolean directorNameValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([\\s.a-zA-Z])*")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }
    public static boolean capacityValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[0-9]+")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }
    public static boolean contactNumberValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[0-9]{3}[-][0-9]{7}")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }

    public static boolean emailValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-z]+([.0-9a-z])*[@]([a-z])+[.]([a-z.])*")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }
    public static boolean usernameValidation(JFXTextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([@.a-zA-Z0-9])*")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }

    public static boolean passwordValidation(JFXPasswordField t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !(t.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[#$^+=!*()@%&]).{8,}"))) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean numberValidation(TextField t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !(t.getText().matches("^[0-9]+"))) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean dateValidation(JFXDatePicker t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getValue()==null){
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean userIDValidation(Label t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getText().equals("UXXX") || t.getText().equals("HXXX") || t.getText().equals("QXXX")) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

}
