import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class ServerService {
    ServerService() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource(
                        "auth.json"
                )).getPath());
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://tugasbesarc.firebaseio.com")
                .build();
        FirebaseApp.initializeApp(options);
    }

}
