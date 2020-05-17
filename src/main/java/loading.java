import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.Objects;

public class loading {
    Scene getScene(){
        GridPane loadGrid = new GridPane();
        loadGrid.setAlignment(Pos.CENTER);
        loadGrid.setPadding(new Insets(50, 50, 50, 50));
        ImageView imageview = new ImageView();

        try{
            Image i = new Image(new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(
                    "load.gif"
            ).getPath())).toURI().toString());
            imageview.setImage(i);
        }catch (Exception ignored){}


        loadGrid.add(imageview ,1,1);

        return new Scene(loadGrid, 300, 275);
    }
}
