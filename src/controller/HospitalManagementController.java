package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import dbConnection.DBConnection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;
import util.HospitalTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class HospitalManagementController {
    public Label lbl_hospital_id;
    public JFXTextField txt_hospital_name;
    public JFXTextField txt_hospital_city;
    public ComboBox<String> cmb_district;
    public JFXTextField txt_hospital_capacity;
    public JFXTextField txt_hospital_director_name;
    public JFXTextField txt_hospital_director_contact;
    public JFXTextField txt_hospital_contact_no1;
    public JFXTextField txt_hospital_contact_no2;
    public JFXTextField txt_hospital_fax;
    public JFXTextField txt_hospital_email;
    public JFXButton btn_hospital_save;
    public JFXButton btn_hospital_delete;
    public JFXButton btn_hospital_add;
    public JFXListView<HospitalTM> ltb_all_hospital;
    public TextField txt_search_hospital;
    public Label label_1;
    public Label label_city;
    public Label label_district;
    public Label label_capacity;
    public Label label_director_name;
    public Label label_directorContact;
    public Label label_contact1;
    public Label label_contact2;
    public Label label_fax;
    public Label label_email;
    public AnchorPane root;
    public Label lbl_hospital_id_error;

    private ArrayList<HospitalTM> hospitalTMArrayList = new ArrayList<>();

    public void initialize() {
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        Platform.runLater(() ->
                {
                    btn_hospital_add.requestFocus();
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
       /* String[] array={" Colombo\n",
                " Gampaha\n",
                " Kalutara\n",
                " Kandy\n",
                " Matale\n",
                " Nuwara Eliya\n",
                " Galle\n"
                };
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Colombo");
        strings.add("Nogombo");
        ObservableList<String> strings2 = FXCollections.observableArrayList(array);
        ObservableList<String> strings1 = FXCollections.observableArrayList(strings);*/

        //load hospitals;
        loadAllHospitals();

        //Selection of the list_box
        ltb_all_hospital.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HospitalTM>() {
            @Override
            public void changed(ObservableValue<? extends HospitalTM> observable, HospitalTM oldValue, HospitalTM newValue) {
                if (newValue == null) {
                    ltb_all_hospital.getSelectionModel().clearSelection();
                } else {
                    lbl_hospital_id.setText(newValue.getHospital_id());
                    txt_hospital_name.setText(newValue.getHospital_name());
                    txt_hospital_city.setText(newValue.getHospital_city());
                    cmb_district.getSelectionModel().select(newValue.getHospital_district());
                    txt_hospital_capacity.setText(newValue.getHospital_capacity());
                    txt_hospital_director_name.setText(newValue.getHospital_director_name());
                    txt_hospital_director_contact.setText(newValue.getHospital_director_contact());
                    txt_hospital_contact_no1.setText(newValue.getHospital_contact_no1());
                    txt_hospital_contact_no2.setText(newValue.getHospital_contact_no2());
                    txt_hospital_fax.setText(newValue.getHospital_fax_number());
                    txt_hospital_email.setText(newValue.getHospital_email());

                    btn_hospital_save.setText("UPDATE");
                }
            }
        });

        //search property
        txt_search_hospital.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<HospitalTM> hospitalTMObservableList = ltb_all_hospital.getItems();
                ltb_all_hospital.getItems().clear();
                for (HospitalTM hospitals : hospitalTMArrayList) {
                    if (hospitals.getHospital_name().contains(newValue)) {
                        hospitalTMObservableList.add(hospitals);
                    }
                }
            }
        });

    }

    public void btn_hospital_save_OnAction(ActionEvent actionEvent) {
        HospitalTM selectedHospital = ltb_all_hospital.getSelectionModel().getSelectedItem();

        String id = lbl_hospital_id.getText();
        String name = txt_hospital_name.getText();
        String city = txt_hospital_city.getText();
        String selectedDistrict = cmb_district.getSelectionModel().getSelectedItem();
        String hospital_capacity = txt_hospital_capacity.getText();
        String hospital_director_name = txt_hospital_director_name.getText();
        String hospital_director_contact = txt_hospital_director_contact.getText();
        String contact_01 = txt_hospital_contact_no1.getText();
        String contact_02 = txt_hospital_contact_no2.getText();
        String hospital_fax = txt_hospital_fax.getText();
        String hospital_email = txt_hospital_email.getText();

        //Form Validation................
        boolean nameValidation = FormValidation.nameValidation(txt_hospital_name, label_1, "* insert a valid hospital name(only letters)");
        boolean cityValidation = FormValidation.cityValidation(txt_hospital_city, label_city, "* insert a valid city name");
        boolean districtValidation = FormValidation.comboboxValidation(cmb_district, label_district, "* select a district");
        boolean directorNameValidation = FormValidation.directorNameValidation(txt_hospital_director_name, label_director_name, "* insert a valid director name");
        boolean capacityValidation = FormValidation.capacityValidation(txt_hospital_capacity, label_capacity, "* insert a valid capacity number");
        boolean contactNumberValidation = FormValidation.contactNumberValidation(txt_hospital_director_contact, label_directorContact, "* xxx-xxxxxxx");
        boolean contact1NumberValidation = FormValidation.contactNumberValidation(txt_hospital_contact_no1, label_contact1, "* xxx-xxxxxxx");
        boolean contact2NumberValidation = FormValidation.contactNumberValidation(txt_hospital_contact_no2, label_contact2, "* xxx-xxxxxxx");
        boolean faxNumberValidation = FormValidation.contactNumberValidation(txt_hospital_fax, label_fax, "* xxx-xxxxxxx");
        boolean emailValidation = FormValidation.emailValidation(txt_hospital_email, label_email, "* name@example.com");
        boolean userIDValidation = FormValidation.userIDValidation(lbl_hospital_id, lbl_hospital_id_error, "* required hospital_id,press add button");

        if(nameValidation && cityValidation && districtValidation && directorNameValidation && capacityValidation && contactNumberValidation &&
                contact1NumberValidation && contact2NumberValidation && faxNumberValidation && emailValidation && userIDValidation) {
            if (btn_hospital_save.getText().equals("SAVE")) {

                String sql = "INSERT INTO hospital(hospital_id,hospital_name,city,district,capacity,director_name,director_contact," +
                        "hospital_contact_01,hospital_contact_02,hospital_fax_number,hospital_email) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1, id);
                    preparedStatement.setObject(2, name);
                    preparedStatement.setObject(3, city);
                    preparedStatement.setObject(4, selectedDistrict);
                    preparedStatement.setObject(5, hospital_capacity);
                    preparedStatement.setObject(6, hospital_director_name);
                    preparedStatement.setObject(7, hospital_director_contact);
                    preparedStatement.setObject(8, contact_01);
                    preparedStatement.setObject(9, contact_02);
                    preparedStatement.setObject(10, hospital_fax);
                    preparedStatement.setObject(11, hospital_email);
                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Hospital added successfully", ButtonType.OK).showAndWait();
                        loadAllHospitals();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Can not add Hospitals,Check with admin", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                String sql = "UPDATE hospital SET hospital_id=?,hospital_name=?,city=?,district=?,capacity=?,director_name=?,director_contact=?," +
                        "hospital_contact_01=?,hospital_contact_02=?,hospital_fax_number=?,hospital_email=? WHERE hospital_id=?";
                try {
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1, id);
                    preparedStatement.setObject(2, name);
                    preparedStatement.setObject(3, city);
                    preparedStatement.setObject(4, selectedDistrict);
                    preparedStatement.setObject(5, hospital_capacity);
                    preparedStatement.setObject(6, hospital_director_name);
                    preparedStatement.setObject(7, hospital_director_contact);
                    preparedStatement.setObject(8, contact_01);
                    preparedStatement.setObject(9, contact_02);
                    preparedStatement.setObject(10, hospital_fax);
                    preparedStatement.setObject(11, hospital_email);
                    preparedStatement.setObject(12, selectedHospital.getHospital_id());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).showAndWait();
                        ltb_all_hospital.getSelectionModel().clearSelection();
                        btn_hospital_save.setText("SAVE");
                        ltb_all_hospital.refresh();
                        loadAllHospitals();
                    } else {
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            clearForm();
        }


    }

    public void btn_hospital_delete_OnAction(ActionEvent actionEvent) {
        if (ltb_all_hospital.getSelectionModel().getSelectedItem()==null) {
            new Alert(Alert.AlertType.ERROR, "Please select a hospital to delete", ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the hospital", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult()==ButtonType.YES) {
            HospitalTM selectedHospital = ltb_all_hospital.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM hospital WHERE hospital_id=?");
                preparedStatement.setObject(1, selectedHospital.getHospital_id());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK).showAndWait();

                    ltb_all_hospital.getSelectionModel().clearSelection();
                    ltb_all_hospital.refresh();
                    loadAllHospitals();
                    btn_hospital_save.setText("SAVE");
                    clearForm();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            clearForm();
            ltb_all_hospital.getSelectionModel().clearSelection();
            btn_hospital_save.setText("SAVE");
            Platform.runLater(() ->
                    {
                        btn_hospital_add.requestFocus();
                    }
            );
        }

    }

    public void btn_hospital_add_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT hospital_id FROM hospital ORDER BY hospital_id DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                lbl_hospital_id.setText("H001");
            } else {
                String last_row_id = resultSet.getString(1);
                String substring = last_row_id.substring(1, 4);
                int hospital_id = Integer.parseInt(substring) + 1;
                if (hospital_id < 10) {
                    lbl_hospital_id.setText("H00" + hospital_id);
                } else if (hospital_id < 100) {
                    lbl_hospital_id.setText("H0" + hospital_id);
                } else {
                    lbl_hospital_id.setText("H" + hospital_id);
                }
            }
            txt_hospital_name.requestFocus();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadAllHospitals() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM hospital");
            ResultSet resultSet = preparedStatement.executeQuery();
            ltb_all_hospital.getItems().clear();
            while (resultSet.next()) {
                String hospital_id = resultSet.getString(1);
                String hospital_name = resultSet.getString(2);
                String hospital_city = resultSet.getString(3);
                String hospital_district = resultSet.getString(4);
                int hospital_capacity = resultSet.getInt(5);
                String hospital_director_name = resultSet.getString(6);
                String hospital_director_contact = resultSet.getString(7);
                String hospital_contact_no1 = resultSet.getString(8);
                String hospital_contact_no2 = resultSet.getString(9);
                String hospital_fax_number = resultSet.getString(10);
                String hospital_email = resultSet.getString(11);

                HospitalTM hospitalDetail = new HospitalTM(hospital_id, hospital_name, hospital_city, hospital_district, hospital_capacity + "",
                        hospital_director_name, hospital_director_contact, hospital_contact_no1, hospital_contact_no2, hospital_fax_number, hospital_email);
                ltb_all_hospital.getItems().add(hospitalDetail);
                hospitalTMArrayList.add(hospitalDetail);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void clearForm(){
        lbl_hospital_id.setText("HXXX");
        txt_hospital_name.clear();
        txt_hospital_city.clear();
        cmb_district.getSelectionModel().clearSelection();
        txt_hospital_capacity.clear();
        txt_hospital_director_name.clear();
        txt_hospital_director_contact.clear();;
        txt_hospital_contact_no2.clear();
        txt_hospital_contact_no1.clear();
        txt_hospital_fax.clear();
        txt_hospital_email.clear();
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
