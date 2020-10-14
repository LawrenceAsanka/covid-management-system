package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;
import util.UserTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class UserManagementController {
    private final ArrayList<UserTM> userTMArrayList = new ArrayList<>();
    public JFXTextField txt_user_first_name;
    public JFXTextField txt_contact;
    public JFXTextField txt_email;
    public JFXTextField txt_username;
    public ComboBox<String> cmb_userRole;
    public JFXButton btn_save;
    public JFXButton btn_delete;
    public JFXButton btn_add_user;
    public ComboBox<String> cmb_select_location;
    public JFXTextField txt_password_show;
    public JFXPasswordField txt_password_hide;
    public FontAwesomeIconView img_password_show;
    public FontAwesomeIconView img_password_hide;
    public Label label_user_id;
    public TableView<UserTM> tbl_userData;
    public JFXTextField txt_search_users;
    public Label label_error_first_name;
    public Label label_contact_error;
    public Label label_error_email;
    public Label label_error_username;
    public Label label_error_password;
    public Label label_error_role;
    public Label label_error_location;
    public JFXButton btn_home;
    public AnchorPane root;
    public Label label_error_userID;


    public void initialize() {
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        Platform.runLater(() ->
                {
                    btn_add_user.requestFocus();
                }
        );


        cmb_select_location.setVisible(false);
        txt_password_show.setVisible(false);
        img_password_show.setVisible(false);

        //Values added to combo box
//        cmb_userRole.getItems().addAll("Admin","P.S.T.F","Hospital ID","Quarantine IT");
        cmb_userRole.getItems().add("Admin");
        cmb_userRole.getItems().add("P.S.T.F");
        cmb_userRole.getItems().add("Hospital IT");
        cmb_userRole.getItems().add("Quarantine IT");

        //ComboBox selection.............................
        cmb_userRole.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue==null){
                    return;
                }
                if (newValue.intValue() == 2) {
                    cmb_select_location.setVisible(true);
                    cmb_select_location.setPromptText("Select Hospital");
                    cmb_select_location.getSelectionModel().clearSelection();
                    cmb_select_location.getSelectionModel().select(null);
                    cmb_select_location.getItems().clear();
                    loadAllHospital();


//                    cmb_select_location.setPromptText((newValue.intValue()==2)? "Select Hospital":"Select Quarantine Center");
                } else if (newValue.intValue() == 3) {
                    cmb_select_location.setVisible(true);
                    cmb_select_location.setPromptText("Select Quarantine Center");
                    cmb_select_location.getSelectionModel().clearSelection();
                    cmb_select_location.getSelectionModel().select(null);
                    cmb_select_location.getItems().clear();
                    loadAllQuarantineCenter();
                } else {
                    cmb_select_location.setVisible(false);
                }
            }
        });


        //load userdata to table...............
        tbl_userData.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tbl_userData.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("userName"));
        tbl_userData.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("role"));
        loadAllUsers();

        //userTable selection.............
        tbl_userData.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {
            @Override
            public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {
                UserTM selectedUser = tbl_userData.getSelectionModel().getSelectedItem();
                if (selectedUser==null) {
                    return;
                }

                if (newValue.getRole().equals("Hospital IT") || newValue.getRole().equals("Quarantine IT")) {

                    label_user_id.setText(selectedUser.getUser_id());
                    txt_user_first_name.setText(selectedUser.getFirst_name());
                    txt_contact.setText(selectedUser.getContact_number());
                    txt_email.setText(selectedUser.getEmail());
                    txt_username.setText(selectedUser.getUserName());
                    txt_password_hide.setText(selectedUser.getPassword());
                    cmb_userRole.getSelectionModel().select(newValue.getRole());
                    cmb_select_location.getSelectionModel().select(newValue.getLocation());

                    cmb_select_location.setVisible(true);
                } else {

                    label_user_id.setText(selectedUser.getUser_id());
                    txt_user_first_name.setText(selectedUser.getFirst_name());
                    txt_contact.setText(selectedUser.getContact_number());
                    txt_email.setText(selectedUser.getEmail());
                    txt_username.setText(selectedUser.getUserName());
                    txt_password_hide.setText(selectedUser.getPassword());
                    cmb_userRole.getSelectionModel().select(newValue.getRole());

                }

                btn_save.setText("UPDATE");

            }
        });

        txt_password_hide.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txt_password_show.setText(newValue);

            }
        });

        txt_password_show.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txt_password_hide.setText(newValue);

            }
        });

        txt_username.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                txt_password_hide.setText(generatePassword(8));
            }
        });

        //search...............
        txt_search_users.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                UserTM selectedUser = tbl_userData.getSelectionModel().getSelectedItem();
                tbl_userData.getItems().clear();
                for (UserTM users : userTMArrayList) {
                    if (users.getFirst_name().contains(newValue) || users.getUserName().contains(newValue)) {
                        tbl_userData.getItems().add(users);
                    }
                }
            }
        });
    }

    public void btn_add_user_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT user_id FROM user ORDER BY user_id DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                label_user_id.setText("U001");
            } else {
                String last_id = resultSet.getString(1);
                String substring_id = last_id.substring(1, 4);
                int new_id = Integer.parseInt(substring_id) + 1;
                if (new_id < 10) {
                    label_user_id.setText("U00" + new_id);
                } else if (new_id < 100) {
                    label_user_id.setText("U0" + new_id);
                } else {
                    label_user_id.setText("U" + new_id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        txt_user_first_name.requestFocus();
    }

    public void btn_save_OnAction(ActionEvent actionEvent) {

        //form Validation...................
        boolean nameValidation = FormValidation.nameValidation(txt_user_first_name, label_error_first_name, "* insert a valid hospital name(only letters)");
        boolean userNameValidation = FormValidation.usernameValidation(txt_username, label_error_username, "* insert a valid user name(only a-z,A-Z,0-9,@,.)");
        boolean contactNumberValidation = FormValidation.contactNumberValidation(txt_contact, label_contact_error, "* xxx-xxxxxxx");
        boolean roleValidation = FormValidation.comboboxValidation(cmb_userRole, label_error_role, "* select a role");
        boolean emailValidation = FormValidation.emailValidation(txt_email, label_error_email, "* name@example.com");
        boolean passwordValidation = FormValidation.passwordValidation(txt_password_hide, label_error_password, "* password length should be 8 more\n,* At least one Capital letter,simple letter,number and  a special character");
        boolean userIDValidation = FormValidation.userIDValidation(label_user_id, label_error_userID, "* required,press add button");

        if (emailValidation&& userNameValidation && contactNumberValidation && roleValidation && nameValidation && passwordValidation && userIDValidation) {

            if (btn_save.getText().equals("SAVE")) {

                //check username is taken or not....................
                try {
                    PreparedStatement pst1 = DBConnection.getInstance().getConnection().prepareStatement("SELECT username FROM user WHERE username=?");
                    pst1.setObject(1, txt_username.getText());
                    ResultSet resultSet = pst1.executeQuery();
                    if (resultSet.next()) {
                        new Alert(Alert.AlertType.ERROR, "username is all ready taken,try another one", ButtonType.OK).showAndWait();
                        txt_username.selectAll();
                        txt_username.requestFocus();
                    } else {

                        //send data to database...............
                        if (cmb_userRole.getSelectionModel().getSelectedIndex() == 2) {
                            boolean locationValidation = FormValidation.comboboxValidation(cmb_select_location, label_error_location, "* select a location");

                            if(locationValidation){
                            try {
                                String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                                preparedStatement.setObject(1, label_user_id.getText());
                                preparedStatement.setObject(2, txt_user_first_name.getText());
                                preparedStatement.setObject(3, txt_contact.getText());
                                preparedStatement.setObject(4, txt_email.getText());
                                preparedStatement.setObject(5, cmb_userRole.getSelectionModel().getSelectedItem());
                                preparedStatement.setObject(6, txt_username.getText());
                                preparedStatement.setObject(7, txt_password_hide.getText());
                                preparedStatement.setObject(8, cmb_select_location.getSelectionModel().getSelectedItem());

                                String selectedItem = cmb_select_location.getSelectionModel().getSelectedItem();
                                String selected_hospital_id = selectedItem.substring(0, 4);

                                int affectedRows = preparedStatement.executeUpdate();
                                if (affectedRows > 0) {

                                    //update hospital table status as reserved................
                                    PreparedStatement preparedStatement1 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                                    preparedStatement1.setObject(1, "reserved");
                                    preparedStatement1.setObject(2, selected_hospital_id);
                                    preparedStatement1.executeUpdate();
                                    new Alert(Alert.AlertType.INFORMATION, "user added successfully", ButtonType.OK).showAndWait();
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "user add failed,try again", ButtonType.OK).showAndWait();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            tbl_userData.getItems().clear();
                            loadAllUsers();
                            tbl_userData.refresh();
                            clearForm();
                            btn_add_user.requestFocus();
                        }
                        } else if (cmb_userRole.getSelectionModel().getSelectedIndex() == 3) {
                            boolean locationValidation = FormValidation.comboboxValidation(cmb_select_location, label_error_location, "* select a location");

                            if(locationValidation){
                            try {
                                String sql = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                                preparedStatement.setObject(1, label_user_id.getText());
                                preparedStatement.setObject(2, txt_user_first_name.getText());
                                preparedStatement.setObject(3, txt_contact.getText());
                                preparedStatement.setObject(4, txt_email.getText());
                                preparedStatement.setObject(5, cmb_userRole.getSelectionModel().getSelectedItem());
                                preparedStatement.setObject(6, txt_username.getText());
                                preparedStatement.setObject(7, txt_password_hide.getText());
                                preparedStatement.setObject(8, cmb_select_location.getSelectionModel().getSelectedItem());

                                String selectedItem = cmb_select_location.getSelectionModel().getSelectedItem();
                                String selected_quarantine_id = selectedItem.substring(0, 4);

                                int affectedRows = preparedStatement.executeUpdate();
                                if (affectedRows > 0) {
                                    //update quarantine center's status as reserved........................
                                    PreparedStatement preparedStatement1 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                                    preparedStatement1.setObject(1, "reserved");
                                    preparedStatement1.setObject(2, selected_quarantine_id);
                                    preparedStatement1.executeUpdate();
                                    new Alert(Alert.AlertType.INFORMATION, "user added successfully", ButtonType.OK).showAndWait();
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "user add failed,try again", ButtonType.OK).showAndWait();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                                tbl_userData.getItems().clear();
                                loadAllUsers();
                                tbl_userData.refresh();
                                clearForm();
                                btn_add_user.requestFocus();
                        }
                        } else {

                            try {
                                String sql = "INSERT INTO user(user_id,first_name,contact_number,email,role,username,password) VALUES (?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                                preparedStatement.setObject(1, label_user_id.getText());
                                preparedStatement.setObject(2, txt_user_first_name.getText());
                                preparedStatement.setObject(3, txt_contact.getText());
                                preparedStatement.setObject(4, txt_email.getText());
                                preparedStatement.setObject(5, cmb_userRole.getSelectionModel().getSelectedItem());
                                preparedStatement.setObject(6, txt_username.getText());
                                preparedStatement.setObject(7, txt_password_hide.getText());

                                int affectedRows = preparedStatement.executeUpdate();
                                if (affectedRows > 0) {
                                    new Alert(Alert.AlertType.INFORMATION, "user added successfully", ButtonType.OK).showAndWait();
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "user add failed,try again", ButtonType.OK).showAndWait();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            tbl_userData.getItems().clear();
                            loadAllUsers();
                            tbl_userData.refresh();
                            clearForm();
                            btn_add_user.requestFocus();
                        }


                }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                //update...................................
                if (txt_username.getText().equals(tbl_userData.getSelectionModel().getSelectedItem().getUserName())) {

                    if (tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("Hospital IT")) {
                        hospitalITUpdate();

                    } else if (tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("Quarantine IT")) {
                        quarantineCenterITUpdate();
                    } else if (tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("Admin") || tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("P.S.T.F")) {
                        adminAndPSTFUpdate();
                    }



                } else {
                    try {
                        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT username FROM user WHERE username=?");
                        preparedStatement.setObject(1, txt_username.getText());
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            new Alert(Alert.AlertType.ERROR, "username is all ready taken,try another one", ButtonType.OK).showAndWait();
                            txt_username.selectAll();
                            txt_username.requestFocus();
                        } else {
                            if (tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("Hospital IT")) {
                                hospitalITUpdate();
                            } else if (tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("Quarantine IT")) {
                                quarantineCenterITUpdate();
                            } else if (tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("Admin") || tbl_userData.getSelectionModel().getSelectedItem().getRole().equals("P.S.T.F")) {
                                adminAndPSTFUpdate();
                            }


                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }

            }
        }
    }

    public void btn_delete_OnAction(ActionEvent actionEvent) {
        UserTM selectedUser = tbl_userData.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete", ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected user", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            //HospitalIT delete................................
            if (cmb_userRole.getSelectionModel().getSelectedIndex() == 2) {
                try {
                    String selected_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0, 4);

                    PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                    preparedStatement3.setObject(1, "not reserved");
                    preparedStatement3.setObject(2, substring_location_id);
                    int ars = preparedStatement3.executeUpdate();

                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM user WHERE user_id=?");
                    preparedStatement.setObject(1, selectedUser.getUser_id());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                //QuarantineIT delete...................
            } else if (cmb_userRole.getSelectionModel().getSelectedIndex() == 3) {
                try {
                    String selected_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0, 4);

                    PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                    preparedStatement3.setObject(1, "not reserved");
                    preparedStatement3.setObject(2, substring_location_id);
                    int afs = preparedStatement3.executeUpdate();

                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM user WHERE user_id=?");
                    preparedStatement.setObject(1, selectedUser.getUser_id());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                //admin and PSTF delete................
                try {
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM user WHERE user_id=?");
                    preparedStatement.setObject(1, selectedUser.getUser_id());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            tbl_userData.getItems().clear();
            tbl_userData.refresh();
            loadAllUsers();
            clearForm();
            btn_add_user.requestFocus();
        } else {
            tbl_userData.refresh();
            clearForm();
            btn_add_user.requestFocus();
        }
    }

    //load all hospital to combo box..................
    private void loadAllHospital() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM hospital WHERE status=?");
            preparedStatement.setObject(1, "not reserved");
            ResultSet resultSet = preparedStatement.executeQuery();
            cmb_select_location.getItems().clear();
            while (resultSet.next()) {
                String hospital_id = resultSet.getString(1);
                String hospital_name = resultSet.getString(2);

                cmb_select_location.getItems().add(hospital_id + " - " + hospital_name);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //hide the password.................
    public void img_password_hide_OnMouseClicked(MouseEvent mouseEvent) {
        txt_password_hide.setVisible(false);
        img_password_hide.setVisible(false);
        img_password_show.setVisible(true);
        txt_password_show.setVisible(true);

        txt_password_show.requestFocus();
        txt_password_show.deselect();
        txt_password_show.positionCaret(txt_password_show.getLength());
    }

    //show the password.....................
    public void img_password_show_OnMouseClicked(MouseEvent mouseEvent) {
        img_password_show.setVisible(false);
        txt_password_show.setVisible(false);
        txt_password_hide.setVisible(true);
        img_password_hide.setVisible(true);

        txt_password_hide.requestFocus();
        txt_password_hide.deselect();
        txt_password_hide.positionCaret(txt_password_hide.getLength());
    }

    //load qc to combo box......................
    private void loadAllQuarantineCenter() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM quarantine_center WHERE status=?");
            preparedStatement.setObject(1, "not reserved");
            ResultSet resultSet = preparedStatement.executeQuery();
            cmb_select_location.getItems().clear();
            while (resultSet.next()) {
                String qc_id = resultSet.getString(1);
                String qc_name = resultSet.getString(2);

                cmb_select_location.getItems().add(qc_id + " - " + qc_name);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //load All users.....................
    private void loadAllUsers() {
        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String contact = resultSet.getString(3);
                String email = resultSet.getString(4);
                String role = resultSet.getString(5);
                String username = resultSet.getString(6);
                String password = resultSet.getString(7);
                String location = resultSet.getString(8);

                UserTM user = new UserTM(id, name, contact, email, role, username, password, location);
                tbl_userData.getItems().add(user);
                userTMArrayList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //hospital update method.............
    private void hospitalITUpdate() {
        if (cmb_userRole.getSelectionModel().getSelectedIndex() == 0 || cmb_userRole.getSelectionModel().getSelectedIndex() == 1) {
            String selected_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
            String substring_location_id = selected_location.substring(0, 4);

            try {
                PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                preparedStatement3.setObject(1, "not reserved");
                preparedStatement3.setObject(2, substring_location_id);
                int affectedRows = preparedStatement3.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("elakiri234");
                }
                updateSQLOfAdminAndPSTF();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tblRefresh();
        } else if (cmb_userRole.getSelectionModel().getSelectedIndex() == 2) {

                if (!cmb_select_location.getSelectionModel().getSelectedItem().equals(tbl_userData.getSelectionModel().getSelectedItem().getLocation())) {
                    String selectedItem = cmb_select_location.getSelectionModel().getSelectedItem();
                    String selected_hospital_id = selectedItem.substring(0, 4);

                    try {
                        PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                        preparedStatement2.setObject(1, "reserved");
                        preparedStatement2.setObject(2, selected_hospital_id);
                        int i = preparedStatement2.executeUpdate();
                        if (i > 0) {
                            System.out.println("elakiri");
                        }

                        String selected_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
                        String substring_location_id = selected_location.substring(0, 4);

                        PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                        preparedStatement3.setObject(1, "not reserved");
                        preparedStatement3.setObject(2, substring_location_id);
                        int affectedRows = preparedStatement3.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("elakiri234");
                        }


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                updateSQLOfLocation();
                tblRefresh();
        } else {
            boolean locationValidation = FormValidation.comboboxValidation(cmb_select_location, label_error_location, "* select a location");

            if(locationValidation){
            String selected_hospital_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
            String selected_hospital_location_id = selected_hospital_location.substring(0, 4);

            try {
                PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                preparedStatement3.setObject(1, "not reserved");
                preparedStatement3.setObject(2, selected_hospital_location_id);
                int affectedRows = preparedStatement3.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("elakiri234");
                }
                String selected_QuarantineCenter = cmb_select_location.getSelectionModel().getSelectedItem();
                String selected_QuarantineCenter_id = selected_QuarantineCenter.substring(0, 4);


                PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                preparedStatement2.setObject(1, "reserved");
                preparedStatement2.setObject(2, selected_QuarantineCenter_id);
                int i = preparedStatement2.executeUpdate();
                if (i > 0) {
                    System.out.println("elakiri");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            updateSQLOfLocation();

                tblRefresh();
        }

        }

    }

    //Quarantine Center update method.......
    private void quarantineCenterITUpdate() {
        if (cmb_userRole.getSelectionModel().getSelectedIndex() == 0 || cmb_userRole.getSelectionModel().getSelectedIndex() == 1) {
            String selected_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
            String substring_location_id = selected_location.substring(0, 4);

            try {
                PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                preparedStatement3.setObject(1, "not reserved");
                preparedStatement3.setObject(2, substring_location_id);
                int affectedRows = preparedStatement3.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("elakiri234");
                }
                updateSQLOfAdminAndPSTF();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tblRefresh();
        } else if (cmb_userRole.getSelectionModel().getSelectedIndex() == 2) {
            boolean locationValidation = FormValidation.comboboxValidation(cmb_select_location, label_error_location, "* select a location");

            if(locationValidation){
            String selected_qc_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
            String selected_qc_location_id = selected_qc_location.substring(0, 4);

            try {
                PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                preparedStatement3.setObject(1, "not reserved");
                preparedStatement3.setObject(2, selected_qc_location_id);
                int affectedRows = preparedStatement3.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("elakiri234");
                }
                String selected_hospital = cmb_select_location.getSelectionModel().getSelectedItem();
                String selected_hospital_id = selected_hospital.substring(0, 4);


                PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                preparedStatement2.setObject(1, "reserved");
                preparedStatement2.setObject(2, selected_hospital_id);
                int i = preparedStatement2.executeUpdate();
                if (i > 0) {
                    System.out.println("elakiri");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            updateSQLOfLocation();

                tblRefresh();
        }
        } else {
            if (!cmb_select_location.getSelectionModel().getSelectedItem().equals(tbl_userData.getSelectionModel().getSelectedItem().getLocation())) {
                String selectedItem = cmb_select_location.getSelectionModel().getSelectedItem();
                String selected_quarantine_id = selectedItem.substring(0, 4);

                try {
                    PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                    preparedStatement2.setObject(1, "reserved");
                    preparedStatement2.setObject(2, selected_quarantine_id);
                    int i = preparedStatement2.executeUpdate();
                    if (i > 0) {
                        System.out.println("elakiri");
                    }

                    String selected_location = tbl_userData.getSelectionModel().getSelectedItem().getLocation();
                    String substring_location_id = selected_location.substring(0, 4);

                    PreparedStatement preparedStatement3 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                    preparedStatement3.setObject(1, "not reserved");
                    preparedStatement3.setObject(2, substring_location_id);
                    int affectedRows = preparedStatement3.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("elakiri234");
                    }


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            updateSQLOfLocation();
            tblRefresh();
        }

    }

    //admin and pstf member update method....
    private void adminAndPSTFUpdate() {
        if (cmb_userRole.getSelectionModel().getSelectedIndex() == 0 || cmb_userRole.getSelectionModel().getSelectedIndex() == 1) {
            try {

                String sql = "UPDATE user SET user_id=?,first_name=?,contact_number=?,email=?,role=?,username=?,password=? WHERE user_id=?";
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                preparedStatement.setObject(1, label_user_id.getText());
                preparedStatement.setObject(2, txt_user_first_name.getText());
                preparedStatement.setObject(3, txt_contact.getText());
                preparedStatement.setObject(4, txt_email.getText());
                preparedStatement.setObject(5, cmb_userRole.getSelectionModel().getSelectedItem());
                preparedStatement.setObject(6, txt_username.getText());
                preparedStatement.setObject(7, txt_password_hide.getText());
                preparedStatement.setObject(8, tbl_userData.getSelectionModel().getSelectedItem().getUser_id());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {

                    System.out.println("update success");
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tblRefresh();

        } else if (cmb_userRole.getSelectionModel().getSelectedIndex() == 2) {
            boolean locationValidation = FormValidation.comboboxValidation(cmb_select_location, label_error_location, "* select a location");

            if(locationValidation){
            String selectedItem = cmb_select_location.getSelectionModel().getSelectedItem();
            String selected_hospital_id = selectedItem.substring(0, 4);

            try {
                PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE hospital SET status=? WHERE hospital_id=?");
                preparedStatement2.setObject(1, "reserved");
                preparedStatement2.setObject(2, selected_hospital_id);
                int i = preparedStatement2.executeUpdate();
                if (i > 0) {
                    System.out.println("elakiri");
                }

                updateSQLOfLocation();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tblRefresh();
        }
        } else {
            boolean locationValidation = FormValidation.comboboxValidation(cmb_select_location, label_error_location, "* select a location");

            if(locationValidation){
            String selectedItem = cmb_select_location.getSelectionModel().getSelectedItem();
            String selected_quarantine_id = selectedItem.substring(0, 4);

            try {
                PreparedStatement preparedStatement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE quarantine_center SET status=? WHERE quarantine_id=?");
                preparedStatement2.setObject(1, "reserved");
                preparedStatement2.setObject(2, selected_quarantine_id);
                int i = preparedStatement2.executeUpdate();
                if (i > 0) {
                    System.out.println("elakiri");
                }

                updateSQLOfLocation();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tblRefresh();
        }
        }

    }

    private void updateSQLOfLocation() {
        try {
            String sql = "UPDATE user SET user_id=?,first_name=?,contact_number=?,email=?,role=?,username=?,password=?,location=? WHERE user_id=?";
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, label_user_id.getText());
            preparedStatement.setObject(2, txt_user_first_name.getText());
            preparedStatement.setObject(3, txt_contact.getText());
            preparedStatement.setObject(4, txt_email.getText());
            preparedStatement.setObject(5, cmb_userRole.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(6, txt_username.getText());
            preparedStatement.setObject(7, txt_password_hide.getText());
            preparedStatement.setObject(8, cmb_select_location.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(9, tbl_userData.getSelectionModel().getSelectedItem().getUser_id());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {

                System.out.println("update success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateSQLOfAdminAndPSTF() {
        try {
            String sql = "UPDATE user SET user_id=?,first_name=?,contact_number=?,email=?,role=?,username=?,password=? WHERE user_id=?";
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setObject(1, label_user_id.getText());
            preparedStatement.setObject(2, txt_user_first_name.getText());
            preparedStatement.setObject(3, txt_contact.getText());
            preparedStatement.setObject(4, txt_email.getText());
            preparedStatement.setObject(5, cmb_userRole.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(6, txt_username.getText());
            preparedStatement.setObject(7, txt_password_hide.getText());
            preparedStatement.setObject(8, tbl_userData.getSelectionModel().getSelectedItem().getUser_id());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {

                System.out.println("update success");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Generate Password
    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "#$^+=!*()@%&";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return String.valueOf(password);
    }

    //clear form..........
    private void clearForm(){
        label_user_id.setText("UXXX");
        cmb_userRole.getSelectionModel().clearSelection();
        cmb_select_location.getSelectionModel().clearSelection();
        tbl_userData.getSelectionModel().clearSelection();
        txt_user_first_name.clear();
        txt_contact.clear();
        txt_email.clear();
        txt_username.clear();
        txt_password_hide.clear();
        txt_password_show.clear();
        txt_search_users.clear();
    }

    private void tblRefresh(){
        tbl_userData.getItems().clear();
        loadAllUsers();
        tbl_userData.refresh();
        clearForm();
        btn_save.setText("SAVE");
        btn_add_user.requestFocus();
    }


    public void btn_home_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));
            Scene dashboardScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.centerOnScreen();
            stage.setScene(dashboardScene);
            stage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
