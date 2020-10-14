package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBConnection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;
import util.QuarantineCenterTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class QuarantineCenterManagementController {
    public TextField txt_search_quarantine_center;
    public JFXListView<QuarantineCenterTM> ltb_all_quarantine_centers;
    public Label lbl_quarantine_center_id;
    public JFXTextField txt_quarantine_center_name;
    public JFXTextField txt_quarantine_center_city;
    public ComboBox<String> cmb_district;
    public JFXTextField txt_quarantine_center_capacity;
    public JFXTextField txt_quarantine_center_head_name;
    public JFXTextField txt_quarantine_center_head_contact;
    public JFXTextField txt_quarantine_center_contact_no1;
    public JFXTextField txt_quarantine_center_contact_no2;
    public JFXButton btn_quarantine_center_save;
    public JFXButton btn_quarantine_center_delete;
    public JFXButton btn_quarantine_center_add;
    public Label label_qc_name;
    public Label label_city;
    public Label label_district;
    public Label label_capacity;
    public Label label_head_name;
    public Label label_contact_no;
    public Label label_contact;
    public Label label_contact_02;
    public AnchorPane root;
    public Label label_qc_id_error;

    private  ArrayList<QuarantineCenterTM> quarantineCenterTMArrayList=new ArrayList<>();

    public void initialize() {
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        Platform.runLater(() ->
                {
                    btn_quarantine_center_add.requestFocus();
                }
        );
        String districtsText = " Colombo\n" +
                " Gampaha\n" +
                " Kalutara\n" +
                " Kandy\n" +
                " Matale\n" +
                " Nuwara Eliya\n" +
                " Galle\n" +
                " Matara\n" +
                " Hambantota\n" +
                " Jaffna\n" +
                " Mannar\n" +
                " Vauniya\n" +
                " Mullativue\n" +
                " Ampara\n" +
                " Trincomalee\n" +
                " Batticaloa\n" +
                " Kilinochchi\n" +
                " Kurunegala\n" +
                " Puttalam\n" +
                " Anuradhapura\n" +
                " Polonnaruwa\n" +
                " Badulla\n" +
                " Moneragala\n" +
                " Ratnapura\n" +
                " Kegalle";

        String[] districts = districtsText.split("\n");
        ObservableList<String> olDistricts = FXCollections.observableArrayList(Arrays.asList(districts));
        cmb_district.setItems(olDistricts);

        //load all quarantine centers
        loadAllQuarantineCenters();

        //listview selection
        ltb_all_quarantine_centers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QuarantineCenterTM>() {
            @Override
            public void changed(ObservableValue<? extends QuarantineCenterTM> observable, QuarantineCenterTM oldValue, QuarantineCenterTM newValue) {
                if (newValue == null) {
                    ltb_all_quarantine_centers.getSelectionModel().clearSelection();
                } else {
                    lbl_quarantine_center_id.setText(newValue.getQuarantine_id());
                    txt_quarantine_center_name.setText(newValue.getQuarantine_name());
                    txt_quarantine_center_city.setText(newValue.getQuarantine_city());
                    cmb_district.getSelectionModel().select(newValue.getQuarantine_district());
                    txt_quarantine_center_head_name.setText(newValue.getQuarantine_head_name());
                    txt_quarantine_center_head_contact.setText(newValue.getQuarantine_head_contact());
                    txt_quarantine_center_contact_no1.setText(newValue.getQuarantine_contact_01());
                    txt_quarantine_center_contact_no2.setText(newValue.getQuarantine_contact_02());
                    txt_quarantine_center_capacity.setText(newValue.getQuarantine_capacity());

                    btn_quarantine_center_save.setText("UPDATE");
                }
            }
        });

        //search
        txt_search_quarantine_center.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ltb_all_quarantine_centers.getItems().clear();
                for (QuarantineCenterTM quarantineCenters : quarantineCenterTMArrayList) {
                    if (quarantineCenters.getQuarantine_name().contains(newValue)) {
                        ltb_all_quarantine_centers.getItems().add(quarantineCenters);
                    }
                }
            }
        });
    }

    public void btn_quarantine_center_save_OnAction(ActionEvent actionEvent) {
        String qc_id = lbl_quarantine_center_id.getText();
        String qc_name = txt_quarantine_center_name.getText();
        String qc_city = txt_quarantine_center_city.getText();
        String qc_district = cmb_district.getSelectionModel().getSelectedItem();
        String qc_head_name = txt_quarantine_center_head_name.getText();
        String qc_head_contact = txt_quarantine_center_head_contact.getText();
        String qc_contact_01 = txt_quarantine_center_contact_no1.getText();
        String qc_contact_02 = txt_quarantine_center_contact_no2.getText();
        String qc_capacity = txt_quarantine_center_capacity.getText();

        //Form Validation.............
        boolean nameValidation = FormValidation.nameValidation(txt_quarantine_center_name, label_qc_name, "* insert a valid quarantine center name(only letters)");
        boolean cityValidation = FormValidation.cityValidation(txt_quarantine_center_city, label_city, "* insert a valid city name");
        boolean districtValidation = FormValidation.comboboxValidation(cmb_district, label_district, "* select a district");
        boolean directorNameValidation = FormValidation.directorNameValidation(txt_quarantine_center_head_name, label_head_name, "* insert a valid head name");
        boolean capacityValidation = FormValidation.capacityValidation(txt_quarantine_center_capacity, label_capacity, "* insert a valid capacity number");
        boolean contactNumberValidation = FormValidation.contactNumberValidation(txt_quarantine_center_head_contact, label_contact_no, "* xxx-xxxxxxx");
        boolean contact1NumberValidation = FormValidation.contactNumberValidation(txt_quarantine_center_contact_no1, label_contact, "* xxx-xxxxxxx");
        boolean contact2NumberValidation = FormValidation.contactNumberValidation(txt_quarantine_center_contact_no2, label_contact_02, "* xxx-xxxxxxx");
        boolean userIDValidation = FormValidation.userIDValidation(lbl_quarantine_center_id, label_qc_id_error, "* required id,press add button");

        if (nameValidation && cityValidation && districtValidation && directorNameValidation && capacityValidation && contactNumberValidation &&
                contact1NumberValidation && contact2NumberValidation && userIDValidation) {
            if (btn_quarantine_center_save.getText().equals("SAVE")) {
                String sql = "INSERT INTO quarantine_center(quarantine_id,quarantine_name,quarantine_city,quarantine_district,quarantine_head_name,quarantine_head_contact," +
                        "quarantine_contact_01,quarantine_contact_02,quarantine_capacity)" +
                        " VALUES (?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1, qc_id);
                    preparedStatement.setObject(2, qc_name);
                    preparedStatement.setObject(3, qc_city);
                    preparedStatement.setObject(4, qc_district);
                    preparedStatement.setObject(5, qc_head_name);
                    preparedStatement.setObject(6, qc_head_contact);
                    preparedStatement.setObject(7, qc_contact_01);
                    preparedStatement.setObject(8, qc_contact_02);
                    preparedStatement.setObject(9, qc_capacity);
                    int affectedRow = preparedStatement.executeUpdate();
                    if (affectedRow > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Quarantine Center Added Successfully", ButtonType.OK).showAndWait();
                        loadAllQuarantineCenters();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Contact Admin", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException throwables) {

                }
                clearForm();
                loadAllQuarantineCenters();
            } else {
                String sql = "UPDATE quarantine_center SET quarantine_id=?,quarantine_name=?,quarantine_city=?,quarantine_district=?,quarantine_head_name=?," +
                        "quarantine_head_contact=?,quarantine_contact_01=?,quarantine_contact_02=?,quarantine_capacity=? WHERE quarantine_id=?";
                try {
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1, qc_id);
                    preparedStatement.setObject(2, qc_name);
                    preparedStatement.setObject(3, qc_city);
                    preparedStatement.setObject(4, qc_district);
                    preparedStatement.setObject(5, qc_head_name);
                    preparedStatement.setObject(6, qc_head_contact);
                    preparedStatement.setObject(7, qc_contact_01);
                    preparedStatement.setObject(8, qc_contact_02);
                    preparedStatement.setObject(9, qc_capacity);
                    preparedStatement.setObject(10, ltb_all_quarantine_centers.getSelectionModel().getSelectedItem().getQuarantine_id());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).showAndWait();

                        clearForm();
                        btn_quarantine_center_save.setText("SAVE");
                        ltb_all_quarantine_centers.refresh();
                        loadAllQuarantineCenters();
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }

    public void btn_quarantine_center_delete_OnAction(ActionEvent actionEvent) {
        if (ltb_all_quarantine_centers.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Select a quarantine center to delete").showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM quarantine_center WHERE quarantine_id=?");
                preparedStatement.setObject(1, ltb_all_quarantine_centers.getSelectionModel().getSelectedItem().getQuarantine_id());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    loadAllQuarantineCenters();
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK).showAndWait();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            clearForm();
            btn_quarantine_center_save.setText("SAVE");
            loadAllQuarantineCenters();
            ltb_all_quarantine_centers.refresh();
        } else {
            clearForm();
            btn_quarantine_center_save.setText("SAVE");
        }
    }

    public void btn_quarantine_center_add_OnAction(ActionEvent actionEvent) {
        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT quarantine_id FROM quarantine_center ORDER BY quarantine_id DESC LIMIT 1");
            if (!resultSet.next()) {
                lbl_quarantine_center_id.setText("Q001");
            } else {
                String last_row_id = resultSet.getString(1);
                String substring = last_row_id.substring(1, 4);
                int qc_id = Integer.parseInt(substring) + 1;
                if (qc_id < 10) {
                    lbl_quarantine_center_id.setText("Q00" + qc_id);
                } else if (qc_id < 100) {
                    lbl_quarantine_center_id.setText("Q0" + qc_id);
                } else {
                    lbl_quarantine_center_id.setText("Q" + qc_id);
                }
            }
            txt_quarantine_center_name.requestFocus();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadAllQuarantineCenters() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM quarantine_center");
            ResultSet resultSet = preparedStatement.executeQuery();
            ltb_all_quarantine_centers.getItems().clear();
            while (resultSet.next()) {
                String qc_id = resultSet.getString(1);
                String qc_name = resultSet.getString(2);
                String qc_city = resultSet.getString(3);
                String qc_district = resultSet.getString(4);
                String qc_head_name = resultSet.getString(5);
                String qc_head_contact = resultSet.getString(6);
                String qc_contact_01 = resultSet.getString(7);
                String qc_contact_02 = resultSet.getString(8);
                int qc_capacity = resultSet.getInt(9);

                QuarantineCenterTM quarantineDetail = new QuarantineCenterTM(qc_id, qc_name, qc_city, qc_district, qc_head_name, qc_head_contact, qc_contact_01, qc_contact_02, qc_capacity + "");
                ltb_all_quarantine_centers.getItems().add(quarantineDetail);
                quarantineCenterTMArrayList.add(quarantineDetail);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void clearForm(){
        lbl_quarantine_center_id.setText("QXXX");
        txt_quarantine_center_name.clear();
        txt_quarantine_center_city.clear();
        cmb_district.getSelectionModel().clearSelection();
        txt_quarantine_center_capacity.clear();
        txt_quarantine_center_head_name.clear();
        txt_quarantine_center_head_contact.clear();
        txt_quarantine_center_contact_no1.clear();
        txt_quarantine_center_contact_no2.clear();
        ltb_all_quarantine_centers.getSelectionModel().clearSelection();

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
