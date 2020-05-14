import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

class LoginStage extends ServerService{
    private String user;
    private String pass;

    LoginStage(Stage primaryStage) throws IOException {
        super();
        primaryStage.setTitle("Login Project");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));


        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

        //Deklarasi obj yg digunakan.
        Text scenetitle = new Text ("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Label userName = new Label("User Name : ");
        Label pw = new Label("Pasword");
        TextField userTextField = new TextField();
        PasswordField pwBox = new PasswordField();

        //Menentukan Letak
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(userName, 0, 1);
        grid.add(userTextField, 1, 1);
        grid.add(pw, 0, 2);
        grid.add(pwBox, 1, 2);



        Button In = new Button("Sign In");
        Button Up = new Button("Sign Up");
        HBox hbBtn = new HBox(10);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(In);
        hbBtn.getChildren().add(Up);
        grid.add(hbBtn, 1, 4);

        Label st = new Label("");
        st.setAlignment(Pos.CENTER);
        st.setTextFill(Color.FIREBRICK);
        grid.add(st, 1, 5);

        ValueEventListener AccountLogin = new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  user = dataSnapshot
                          .child("Account")
                          .child("1")
                          .child("username").getValue(String.class);
                  pass = dataSnapshot
                          .child("Account")
                          .child("1")
                          .child("password").getValue(String.class);
              }
              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          };
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(AccountLogin);
        In.setOnAction(e -> {
            if(userTextField.getText().equalsIgnoreCase(user)
                    && pwBox.getText().equalsIgnoreCase(pass))
            {
                st.setText("Login Berhasil");
            }else {
                st.setText("Login Gagal");
            }
        });

        Up.setOnAction(e -> {

	FirebaseDatabase.getInstance().getReference().child("Account")
                          .child("1")
                          .child("username").setValue((userTextField.getText(), null);

        FirebaseDatabase.getInstance().getReference().child("Account")
                          .child("1")
                          .child("password").setValue((pwBox.getText(), null);

        });


        primaryStage.show();
    }




}
