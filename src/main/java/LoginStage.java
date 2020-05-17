import com.google.firebase.database.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Show login form
 */
class LoginStage extends ServerService {
    private String nim;

    /**
     * @param loginStage Define Stage
     * @throws IOException Handle Input Output
     */
    LoginStage(Stage loginStage) throws IOException {
        loginStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(grid, 300, 275);
        loginStage.setScene(scene);

        //Deklarasi obj yg digunakan.
        Text scenetitle = new Text("Masuk");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        Label username = new Label("NIM: ");
        TextField nimTextField = new TextField();

        //Menentukan Letak
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(username, 0, 1);
        grid.add(nimTextField, 1, 1);

        Button In = new Button("Masuk");
        Button Up = new Button("Daftar");
        HBox hbBtn = new HBox(10);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(In);
        hbBtn.getChildren().add(Up);
        grid.add(hbBtn, 1, 3);

        Label st = new Label("");
        st.setAlignment(Pos.CENTER);
        st.setTextFill(Color.FIREBRICK);
        grid.add(st, 1, 4);

        ValueEventListener AccountLogin = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        FirebaseDatabase.getInstance().getReference("Account").addListenerForSingleValueEvent(AccountLogin);
        In.setOnAction(e -> {
            if (nimTextField.getText().equalsIgnoreCase(nim)) {
                st.setText("Login Berhasil");
            } else {
                st.setText("Login Gagal");
            }
        });

        Up.setOnAction(e -> {
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);

            GridPane registerGrid = new GridPane();
            registerGrid.setAlignment(Pos.CENTER);
            registerGrid.setHgap(10);
            registerGrid.setVgap(10);
            registerGrid.setPadding(new Insets(10, 10, 10, 10));

            Scene registerScene = new Scene(registerGrid, 300, 275);
            registerStage.setScene(registerScene);

            //Deklarasi obj yg digunakan.
            Text registerSceneTitle = new Text("Daftar");
            registerSceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            registerGrid.add(registerSceneTitle, 0, 0, 2, 1);

            Label nim = new Label("NIM: ");
            TextField nimField = new TextField();
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

            cancelButton.setOnAction(event -> {
                registerStage.close();
            });

            signUpButton.setOnAction(event -> {
                Alert alertAdd = new Alert(Alert.AlertType.CONFIRMATION, "Apakah data anda sudah benar?", ButtonType.YES, ButtonType.NO);
                alertAdd.showAndWait();
                if (alertAdd.getResult() == ButtonType.YES) {
                    createNewUser(nimField.getText(), nameField.getText(), majorField.getText(), gradeField.getText());
                    Alert alertSuccess = new Alert(Alert.AlertType.NONE, "Pendaftaran anda berhasil. Silahkan masuk terlebih dahulu", ButtonType.YES);
                    alertSuccess.show();
                    registerStage.close();
                }
            });

            registerStage.show();
        });
        loginStage.show();
    }

    public void createNewUser(String nim, String name, String major, String grade) {
        User user = new User(name, major, grade);
        FirebaseDatabase.getInstance().getReference().child("Account").child(nim).setValue(user, null);;
    }
}
