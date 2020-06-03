import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;

public class Loading {
    private int timeSeconds;
    private Timeline timer;

    Scene getScene() {
        GridPane loadGrid = new GridPane();
        loadGrid.setAlignment(Pos.CENTER);
        loadGrid.setPadding(new Insets(50, 50, 50, 50));
        ImageView imageview = new ImageView();

        try {
            Image i = new Image(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(
                    "load.gif"
            ).getPath())).toURI().toString());
            imageview.setImage(i);
        } catch (Exception ignored) {
        }

        loadGrid.add(imageview, 1, 1);

        return new Scene(loadGrid, 300, 275);
    }


    Scene getDaftarNoticeScene(Stage stage, Scene scene, String pesan) {
        GridPane noticeGrid = new GridPane();
        noticeGrid.setAlignment(Pos.CENTER);
        noticeGrid.setPadding(new Insets(50, 50, 50, 50));
        Label notice = new Label(pesan);
        notice.setFont(Font.font(17));
        noticeGrid.add(notice, 1, 1);
        timeSeconds = 2;
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeSeconds--;
            if (timeSeconds <= 0) {
                stage.setScene(scene);
                timer.stop();

            }

        }));
        timer.setCycleCount(5);
        timer.playFromStart();
        return new Scene(noticeGrid, 300, 275);
    }
}
