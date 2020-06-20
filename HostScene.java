import javafx.fxml.*;
import javafx.scene.*;

public class HostScene {
    private Scene scene = null;
    private Parent root = null;

    public HostScene () {
        try {
            root = FXMLLoader.load(getClass().getResource("hostscene.fxml"));
            scene = new Scene (root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene getScene () {return scene;}
}