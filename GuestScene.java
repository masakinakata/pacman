import javafx.fxml.*;
import javafx.scene.*;

public class GuestScene {
    private Scene scene = null;
    private Parent root = null;

    public GuestScene () {
        try {
            root = FXMLLoader.load(getClass().getResource("guestscene.fxml"));
            scene = new Scene (root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene getScene () {return scene;}
}