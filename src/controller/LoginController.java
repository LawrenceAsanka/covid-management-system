package controller;

import com.jfoenix.controls.JFXButton;
import dbConnection.DBConnection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public static String user_id;
    public static String user_name;
    public static String user_role;
    public TextField txt_user_name;
    public TextField txt_password;
    public ComboBox<String> cmb_role;
    public JFXButton btn_login;
    public AnchorPane root;
    public HBox hbox_userName_error;
    public HBox hbox_password_error;
    public HBox hbox_cmb_error;
    public ImageView icon_loading;


    public void initialize() {
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();


        cmb_role.getItems().addAll("Admin", "P.S.T.F", "Hospital IT", "Quarantine IT");
        icon_loading.setVisible(false);


    }

    public void btn_login_OnAction(ActionEvent actionEvent) {
        /*icon_loading.setVisible(true);
        PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.seconds(1));
        pt.setOnFinished(event -> {

        });
        pt.play();*/

        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM user WHERE username=? AND password=? AND role=?");
            preparedStatement.setObject(1, txt_user_name.getText());
            preparedStatement.setObject(2, txt_password.getText());
            preparedStatement.setObject(3, cmb_role.getSelectionModel().getSelectedItem());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user_id = resultSet.getString(1);
                user_role = resultSet.getString(5);
                user_name = resultSet.getString(6);




                try {
                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/dashboard.fxml"));
                    Scene dashboardScene = new Scene(root);
                    Stage stage = (Stage) (this.root.getScene().getWindow());
                    stage.setScene(dashboardScene);
                    stage.centerOnScreen();
                    stage.sizeToScene();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {

                hbox_userName_error.setStyle("-fx-border-color: red;-fx-background-radius: 50px;-fx-border-radius: 50px");
                hbox_password_error.setStyle("-fx-border-color: red;-fx-background-radius: 50px;-fx-border-radius: 50px");


            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    public void icon_exit_OnMouseClicked(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
