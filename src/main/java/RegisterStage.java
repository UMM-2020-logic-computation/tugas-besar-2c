import com.google.firebase.database.FirebaseDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import helper.Validation;
import javafx.util.Duration;


public class RegisterStage {

    private Scene scene;
    private int timeSeconds;
    private Timeline timer;

    /**
     * @param primaryStage Define Stage from LoginStage
     * @param primaryScane Define Scene from LoginStage
     */
    RegisterStage(Stage primaryStage, Scene primaryScane) {
        GridPane registerGrid = new GridPane();
        registerGrid.setAlignment(Pos.CENTER);
        registerGrid.setHgap(10);
        registerGrid.setVgap(10);
        registerGrid.setPadding(new Insets(10, 10, 10, 10));

        //Deklarasi obj yg digunakan.
        Text registerSceneTitle = new Text("Daftar");
        registerSceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        registerGrid.add(registerSceneTitle, 0, 0, 2, 1);

        Label nim = new Label("NIM: ");
        TextField nimField = new TextField();
        // Input can only number
        nimField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nimField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        registerGrid.add(nim, 0, 1);
        registerGrid.add(nimField, 1, 1);

        Label name = new Label("Nama: ");
        TextField nameField = new TextField();
        registerGrid.add(name, 0, 2);
        registerGrid.add(nameField, 1, 2);

        Label major = new Label("Jurusan: ");
        TextField majorField = new TextField();
        registerGrid.add(major, 0, 3);
        registerGrid.add(majorField, 1, 3);

        Label grade = new Label("Kelas: ");
        TextField gradeField = new TextField();
        registerGrid.add(grade, 0, 4);
        registerGrid.add(gradeField, 1, 4);

        Button cancelButton = new Button("Batal");
        Button signUpButton = new Button("Daftar");
        HBox hbBtnRegister = new HBox(10, cancelButton, signUpButton);
        registerGrid.add(hbBtnRegister, 1, 5);

        scene = new Scene(registerGrid, 300, 275);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        cancelButton.setOnAction(event -> {
            primaryStage.setScene(primaryScane);
        });

        signUpButton.setOnAction(event -> {
            try {
                if (!Validation.textIsEmpty(nimField) && !Validation.textIsEmpty(nameField)&& !Validation.textIsEmpty(majorField)&& !Validation.textIsEmpty(gradeField)) {
                    Alert alertAdd = new Alert(Alert.AlertType.CONFIRMATION,
                            "Apakah data anda sudah benar?", ButtonType.YES, ButtonType.NO);
                    alertAdd.showAndWait();
                    if (alertAdd.getResult() == ButtonType.YES) {
                        primaryStage.setScene(new Loading().getScene()); // Loading

                        createNewUser(nimField.getText(), nameField.getText(), majorField.getText(), gradeField.getText());
                        timeSeconds = 5;

                        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                            timeSeconds--;
                            if (timeSeconds <= 0) {
                                /* Error IligalArgumen*/
                                //Alert alertSuccess = new Alert(Alert.AlertType.NONE,
                                //        "Pendaftaran anda berhasil. Silahkan masuk terlebih dahulu", ButtonType.YES);
                                //alertSuccess.showAndWait();
                                //if (alertSuccess.getResult() == ButtonType.YES) {
                                    primaryStage.setScene(primaryScane);
                                //} @Todo <--@NoeSy(Ganti Alert menjadi notice setelah load gif)
                                timer.stop();
                            }
                        }));
                        timer.setCycleCount(5);
                        timer.playFromStart();
                    }
                }
            } catch (NullPointerException e) {
                throw e;
            }
        });
    }

    private void createNewUser(String nim, String name, String major, String grade) {
        timeSeconds = 0;
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("nim").setValue(nim, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("name").setValue(name, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("major").setValue(major, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("grade").setValue(grade, null);
    }

    Scene getScene() {
        return scene;
    }
}

