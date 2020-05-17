import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        new LoginStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

