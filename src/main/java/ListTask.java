
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;


public class ListTask {
    private int countIndex;
    private int indexTask;
     ListTask(Stage taskStage, User user, Scene loginScene) {
        taskStage.setTitle("Kerjain App");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(grid, 600, 410);
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
        grid.add(buttonLogout, 1, 0);
        buttonLogout.setPrefWidth(110);
        buttonLogout.setOnAction(e -> {
            Alert alertAdd = new Alert(Alert.AlertType.CONFIRMATION,
                    "Anda yakin ingin keluar?", ButtonType.YES, ButtonType.NO);
            alertAdd.showAndWait();
            if (alertAdd.getResult() == ButtonType.YES) {
                taskStage.setScene(loginScene);
                new User().clearUser(); // Clear User
            }
        });

        Label titleTask = new Label("Judul:");
        TextField titleField = new TextField();
        titleField.setPrefWidth(240);

        Label dateTask = new Label("Batas Waktu:");
        DatePicker dateField = new DatePicker();
        dateField.setPrefWidth(150);

        HBox boxTitle = new HBox(215, titleTask, dateTask);
        boxTitle.setAlignment(Pos.TOP_LEFT);
        grid.add(boxTitle, 0, 3);

        HBox boxField = new HBox(10, titleField, dateField);
        boxField.setAlignment(Pos.TOP_LEFT);
        grid.add(boxField, 0, 4);

        Button buttonAdd = new Button("Tambah Tugas");
        grid.add(buttonAdd, 1, 4);

        Button doneTaskButton = new Button();
        Image doneIcon = new Image(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("check.png").getPath())).toURI().toString());
        ImageView doneIconView = new ImageView(doneIcon);
        doneIconView.setFitHeight(30);
        doneIconView.setFitWidth(30);
        doneTaskButton.setGraphic(doneIconView);

        Button deleteTaskButton = new Button();
        Image deleteIcon = new Image(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("delete.png").getPath())).toURI().toString());
        ImageView deleteIconView = new ImageView(deleteIcon);
        deleteIconView.setFitHeight(30);
        deleteIconView.setFitWidth(30);
        deleteTaskButton.setGraphic(deleteIconView);

        HBox actionButtons = new HBox(10, doneTaskButton, deleteTaskButton);
        grid.add(actionButtons, 1, 5);

        TableView<Tasks> tableTask = new TableView<Tasks>();
        tableTask.setPlaceholder(new Label("Anda tidak memiliki tugas"));

        TableColumn<Tasks, String> numberColumn = new TableColumn<Tasks, String>("No");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Tasks, String> titleColumn = new TableColumn<Tasks, String>("Judul");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Tasks, String> deadlineColumn = new TableColumn<Tasks, String>("Batas Waktu");
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        tableTask.getColumns().addAll(numberColumn, titleColumn, deadlineColumn);

        // Display row data
        ValueEventListener getTaks = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 1; dataSnapshot.child(user.getNim()).child("tasks").hasChild(String.valueOf(i)); i++) {
                    tableTask.getItems().add(dataSnapshot.child(user.getNim()).child("tasks").child(String.valueOf(i)).getValue(Tasks.class));
                    System.out.println(i);
                    countIndex = i;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        FirebaseDatabase.getInstance().getReference("Account").addListenerForSingleValueEvent(getTaks);
        grid.add(tableTask, 0, 5);

        tableTask.setOnMouseClicked(e ->{
            indexTask = tableTask.getSelectionModel().getFocusedIndex() + 1;
            System.out.println(indexTask);
        });
        buttonAdd.setOnAction(e -> {
            tableTask.getItems().clear();
            createNewTask(user.getNim(), dateField.getValue().toString(), titleField.getText(), countIndex);
            FirebaseDatabase.getInstance().getReference("Account").addListenerForSingleValueEvent(getTaks);
        });
        doneTaskButton.setOnAction(event -> {
            FirebaseDatabase.getInstance().getReference("Account").child(user.getNim()).child("tasks")
                .child(String.valueOf(indexTask)).child("status").setValue("Done", null);
        });
        deleteTaskButton.setOnAction(event -> {
            tableTask.getItems().clear();
            deleteTask(user.getNim(), indexTask);
            FirebaseDatabase.getInstance().getReference("Account").addListenerForSingleValueEvent(getTaks);
        });

        taskStage.show();
    }
    private void createNewTask(String nim, String deadline, String title, int countIndex) {
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(countIndex + 1)).child("deadline").setValue(deadline, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(countIndex + 1)).child("status").setValue("On Progress", null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(countIndex + 1)).child("title").setValue(title, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(countIndex + 1)).child("number").setValue(String.valueOf(countIndex + 1), null);
    }
    private void deleteTask(String nim, int indexDelate) {
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(indexDelate)).child("deadline").removeValue(null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(indexDelate)).child("status").removeValue(null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(indexDelate)).child("title").removeValue(null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(String.valueOf(indexDelate)).child("number").removeValue(null);
    }
}
