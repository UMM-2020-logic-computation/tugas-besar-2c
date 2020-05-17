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
                if(dataSnapshot.hasChild(nimTextField.getText())){
                    nim = dataSnapshot.child(nimTextField.getText()).child("nim").getValue(String.class);
                    System.out.println(nim);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        
        In.setOnAction(e -> {
            FirebaseDatabase.getInstance().getReference("Account").addListenerForSingleValueEvent(AccountLogin);
            if (nimTextField.getText().equalsIgnoreCase(nim)) {
                st.setText("Login Berhasil");
            } else {
                st.setText("Login Gagal");
            }
        });

        Up.setOnAction(e -> {
            loginStage.setScene(new RegisterStage(loginStage, scene).getScene());
        });



        loginStage.show();
    }
}
