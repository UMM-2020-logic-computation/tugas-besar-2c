import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javafx.beans.binding.BooleanBinding;
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

        dateField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$")) {
                dateField.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        buttonAdd.disableProperty().bind(new BooleanBinding() {
            {super.bind(
                    titleField.textProperty(),
                    dateField.getEditor().textProperty()
            );}
            @Override
            protected boolean computeValue() {
                return titleField.getText().isEmpty() || dateField.getEditor().getText().isEmpty();
            }});

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
        //numberColumn.setReorderable(false);\
        numberColumn.setResizable(false);
        numberColumn.setCellFactory(col -> new TableCell<Tasks, String>() {
            @Override
            public void updateIndex(int index) {
                super.updateIndex(index);
                if (isEmpty() || index < 0) {
                    setText(null);
                } else {
                    setText(Integer.toString(index + 1));
                }
            }
        });

        TableColumn<Tasks, String> titleColumn = new TableColumn<Tasks, String>("Judul");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        //titleColumn.setReorderable(false);

        TableColumn<Tasks, String> deadlineColumn = new TableColumn<Tasks, String>("Batas Waktu");
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        //deadlineColumn.setReorderable(false);

        TableColumn<Tasks, String> statusColumn = new TableColumn<Tasks, String>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        //statusColumn.setReorderable(false);

        tableTask.getColumns().addAll(numberColumn, titleColumn, deadlineColumn, statusColumn);

        // Display row data
        ValueEventListener getsTaks = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tableTask.getItems().clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Tasks tasks = snapshot.getValue(Tasks.class);
                    tableTask.getItems().add(tasks);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        FirebaseDatabase.getInstance().getReference("Account").child(user.getNim()).child("tasks").addListenerForSingleValueEvent(getsTaks);
        grid.add(tableTask, 0, 5);

        buttonAdd.setOnAction(e -> {
            createNewTask(user.getNim(), dateField.getValue().toString(), titleField.getText());
            FirebaseDatabase.getInstance().getReference("Account").child(user.getNim()).child("tasks").addListenerForSingleValueEvent(getsTaks);
        });

        doneTaskButton.setOnAction(event -> {
            Tasks task = tableTask.getSelectionModel().getSelectedItem(); // Get selected row
            Alert alertSuccess = new Alert(Alert.AlertType.NONE,
                    "Task ditandai 'Done'.", ButtonType.YES);
            alertSuccess.showAndWait();
            if(alertSuccess.getResult() == ButtonType.YES) {
                FirebaseDatabase.getInstance().getReference("Account")
                        .child(user.getNim()).child("tasks").child(task.getId())
                        .child("status").setValue("Done", null);
                FirebaseDatabase.getInstance().getReference("Account")
                        .child(user.getNim()).child("tasks").addListenerForSingleValueEvent(getsTaks);
            }
        });

        deleteTaskButton.setOnAction(event -> {
            Tasks task = tableTask.getSelectionModel().getSelectedItem(); // Get selected row
            Alert alertSuccess = new Alert(Alert.AlertType.NONE,
                    "Task dihapus.", ButtonType.YES);
            alertSuccess.showAndWait();
            if(alertSuccess.getResult() == ButtonType.YES) {
                deleteTask(user.getNim(), task.getId());
                FirebaseDatabase.getInstance().getReference("Account")
                        .child(user.getNim()).child("tasks").addListenerForSingleValueEvent(getsTaks);
                }
        });

        taskStage.show();
    }

    private void createNewTask(String nim, String deadline, String title) {
        String idTask = getAlphaNumericString(20); // Unique ID for Task
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(idTask).child("deadline").setValue(deadline, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(idTask).child("status").setValue("On Progress", null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(idTask).child("title").setValue(title, null);
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(idTask).child("id").setValue(idTask, null);
    }

    private void deleteTask(String nim, String idTask) {
        FirebaseDatabase.getInstance().getReference("Account").child(nim).child("tasks")
                .child(idTask).removeValue(null);
    }

    private static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
