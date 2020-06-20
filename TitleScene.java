import javafx.fxml.*;
import javafx.scene.*;

public class TitleScene {
    private Scene scene = null;
    private Parent root = null;

    public TitleScene () {
        try {
            root = FXMLLoader.load(getClass().getResource("titlescene.fxml"));
            scene = new Scene (root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene getScene () {return scene;}
}