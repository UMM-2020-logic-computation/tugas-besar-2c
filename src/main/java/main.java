import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (netIsAvailable()) {
            new LoginStage(primaryStage);
        } else {
            Alert alertAdd = new Alert(Alert.AlertType.ERROR,
                    "Pastikan anda terhubung dengan koneksi internet. Silahkan coba lagi", ButtonType.CLOSE);
            alertAdd.showAndWait();
            if (alertAdd.getResult() == ButtonType.CLOSE) {
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}

