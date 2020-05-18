import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ListTask {
    public ListTask(Stage taskStage, User user, Scene loginScene) throws IOException {
        System.out.println(user.getName());
        taskStage.setTitle("Kerjain App");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(grid, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        taskStage.setScene(scene);

        Text sceneTaskTitle = new Text("Selamat datang,");
        sceneTaskTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 22));
        grid.add(sceneTaskTitle, 0, 0, 2, 1);

        Text userTitle = new Text(user.getName());
        userTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        grid.add(userTitle, 0, 1);

        Text userDesc = new Text(user.getNim() + " | " + user.getMajor() + " | " + user.getGrade());
        userDesc.setFont(Font.font("Tahoma", FontWeight.NORMAL, 10));
        grid.add(userDesc, 0, 2);

        Button buttonLogout = new Button("Keluar");
        grid.add(buttonLogout, 2, 1);
        buttonLogout.setOnAction(e -> {
            Alert alertAdd = new Alert(Alert.AlertType.CONFIRMATION,
                    "Anda yakin ingin keluar?", ButtonType.YES, ButtonType.NO);
            alertAdd.showAndWait();
            if (alertAdd.getResult() == ButtonType.YES) {
                taskStage.setScene(loginScene);
                new User().clearUser(); // Clear User
            }
        });

        taskStage.show();
    }
}
