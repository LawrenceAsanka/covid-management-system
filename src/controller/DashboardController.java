package controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {
    public JFXButton btn_covidUpdate;
    public Label lbl_userName;
    public JFXButton btn_hospitalMgt;
    public JFXButton btn_QuarantineMgt;
    public JFXButton btn_userMgt;
    public FontAwesomeIconView btn_logout;
    public AnchorPane root;

    public void initialize(){
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        lbl_userName.setText("Welcome "+LoginController.user_name);
    }

    public void btn_covidUpdate_OnAction(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/covidDataUpdate.fxml"));
            Scene covidUpdateScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(covidUpdateScene);
            stage.centerOnScreen();
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btn_hospitalMgt_OnAction(ActionEvent actionEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/hospitalManagement.fxml"));
            Scene hospitalMgtScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(hospitalMgtScene);
            stage.centerOnScreen();
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }else{
            new Alert(Alert.AlertType.ERROR, "No permission to add hospitals", ButtonType.OK).showAndWait();
        }
    }

    public void btn_QuarantineMgt_OnAction(ActionEvent actionEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {

            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/quarantineCenterManagement.fxml"));
                Scene QuarantineMgtScene = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(QuarantineMgtScene);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No permission to add Quarantine Centers", ButtonType.OK).showAndWait();
        }
    }

    public void btn_userMgt_OnAction(ActionEvent actionEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {

            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/userManagement.fxml"));
                Scene userMgtScene = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(userMgtScene);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No permission to add Users", ButtonType.OK).showAndWait();
        }
    }

    public void icon_logout_OnMouseClicked(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/login.fxml"));
            Scene loginScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(loginScene);
            stage.centerOnScreen();
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void icon_hospitalMgt_OnMouseClicked(MouseEvent mouseEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {
            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/hospitalManagement.fxml"));
                Scene hospitalMgtScene = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(hospitalMgtScene);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "No permission to add hospitals", ButtonType.OK).showAndWait();
        }
    }

    public void icon_qcMgt_OnMouseClicked(MouseEvent mouseEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {

            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/quarantineCenterManagement.fxml"));
                Scene QuarantineMgtScene = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(QuarantineMgtScene);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No permission to add Quarantine Centers", ButtonType.OK).showAndWait();
        }
    }

    public void icon_userMgt_OnMouseClicked(MouseEvent mouseEvent) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {

            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/userManagement.fxml"));
                Scene userMgtScene = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(userMgtScene);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No permission to add Users", ButtonType.OK).showAndWait();
        }
    }

    public void icon_covidUpdate_OnMouseClicked(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/covidDataUpdate.fxml"));
            Scene covidUpdateScene = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(covidUpdateScene);
            stage.centerOnScreen();
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
