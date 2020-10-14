package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import dbConnection.DBConnection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CovidDataUpdateController {
    public JFXButton btn_update;
    public Label lbl_updatedDate;
    public Label lbl_confirmedCases;
    public Label lbl_recoveredCases;
    public Label lbl_deathCases;
    public JFXDatePicker date_picker;
    public TextField txt_confirmedCases;
    public TextField txt_recoveredCases;
    public TextField txt_deathCases;
    public JFXButton btn_home;
    public AnchorPane root;
    public Label label_error_datepicker;
    public Label label_error_confirmedCases;
    public Label label_error_recoveredCases;
    public Label label_error_deathCases;
    public VBox vbox_data_field;
    public VBox vbox_text_field;

    public void initialize() {
        if (LoginController.user_role.equals("Hospital IT") || LoginController.user_role.equals("Quarantine IT")) {
            vbox_text_field.setDisable(true);

        }
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        //limitation of date...........
        LocalDate minDate = LocalDate.of(2019, 11, 1);
        LocalDate maxDate = LocalDate.now();
        date_picker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }
                });

        loadAllEnteredRecord();

    }


    public void btn_update_OnAction(ActionEvent actionEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {

        boolean numberValidation = FormValidation.numberValidation(txt_confirmedCases, label_error_confirmedCases, "* input a number");
        boolean numberValidation1 = FormValidation.numberValidation(txt_recoveredCases, label_error_recoveredCases, "* input a number");
        boolean numberValidation2 = FormValidation.numberValidation(txt_deathCases, label_error_deathCases, "* input a number");
        boolean dateValidation = FormValidation.dateValidation(date_picker, label_error_datepicker, "* required date");

        if (numberValidation && numberValidation1 && numberValidation2 && dateValidation) {
            if (btn_update.getText().equals("ADD NEW RECORD")) {
                try {
                    String sql = "INSERT INTO global_update_detail (admin_id,updated_date,confirmed_count,recovered_count,death_count)" +
                            "VALUES (?,?,?,?,?)";
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1, LoginController.user_id);
                    preparedStatement.setObject(2, date_picker.getValue());
                    preparedStatement.setObject(3, txt_confirmedCases.getText());
                    preparedStatement.setObject(4, txt_recoveredCases.getText());
                    preparedStatement.setObject(5, txt_deathCases.getText());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Record insert Successfully").showAndWait();
                    }
                    loadAllEnteredRecord();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                try {
                    String sql = "UPDATE global_update_detail SET admin_id=?,confirmed_count=?,recovered_count=?,death_count=? WHERE updated_date=?";
                    PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                    preparedStatement.setObject(1, LoginController.user_id);
                    preparedStatement.setObject(2, txt_confirmedCases.getText());
                    preparedStatement.setObject(3, txt_recoveredCases.getText());
                    preparedStatement.setObject(4, txt_deathCases.getText());
                    preparedStatement.setObject(5, date_picker.getValue());
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Record updated Successfully").showAndWait();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                btn_update.setText("ADD NEW RECORD");
                clearTextField();
                date_picker.getEditor().clear();
                date_picker.requestFocus();
                loadAllEnteredRecord();

            }
        }
    }else{
            vbox_text_field.setVisible(false);
            vbox_data_field.setAlignment(Pos.CENTER);

        }
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

    public void date_picker_OnAction(ActionEvent actionEvent) {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM global_update_detail WHERE updated_date=?");
            preparedStatement.setObject(1, date_picker.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                btn_update.setText("UPDATE");
                btn_update.setStyle("-fx-background-color: #f44336");

                txt_confirmedCases.setText(resultSet.getString(4));
                txt_recoveredCases.setText(resultSet.getString(5));
                txt_deathCases.setText(resultSet.getString(6));
            } else {
                clearTextField();
                btn_update.setText("ADD NEW RECORD");
                btn_update.setStyle("-fx-background-color: #4CAF50");
                txt_confirmedCases.requestFocus();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void loadAllEnteredRecord() {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT updated_date FROM global_update_detail ORDER BY updated_date DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                lbl_updatedDate.setText(resultSet.getDate(1) + "");

                //load cases count sum.................
                sumOfCases();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void clearTextField() {
        txt_confirmedCases.clear();
        txt_recoveredCases.clear();
        txt_deathCases.clear();
    }

    private void sumOfCases() {
        try {
            PreparedStatement preparedStatement1 = DBConnection.getInstance().getConnection().prepareStatement("SELECT SUM(confirmed_count),SUM(recovered_count),SUM(death_count) FROM global_update_detail");
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (resultSet1.next()) {
                lbl_confirmedCases.setText(resultSet1.getInt(1) + "");
                lbl_recoveredCases.setText(resultSet1.getInt(2) + "");
                lbl_deathCases.setText(resultSet1.getInt(3) + "");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
